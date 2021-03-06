from common_fixtures import *  # NOQA
import re


@pytest.mark.parametrize('kind', ['user', 'admin'])
def test_account_create(kind, admin_user_client, random_str):
    account = admin_user_client.create_account(kind=kind,
                                               name=random_str)

    assert account.state == "registering"
    assert account.transitioning == "yes"

    account = admin_user_client.wait_success(account)

    assert account.transitioning == "no"
    assert account.state == "active"

    count = len(admin_user_client.list_account(name=random_str))
    assert count == 1

    creds = account.credentials()

    assert len(creds) == 2
    creds = filter(lambda x: x.kind == 'apiKey', creds)

    assert len(creds) == 1
    assert creds[0].state == "active"
    assert creds[0].kind == "apiKey"
    assert re.match("[A-Z]*", creds[0].publicValue)
    assert len(creds[0].publicValue) == 20
    assert re.match("[a-zA-Z0-9]*", creds[0].secretValue)
    assert len(creds[0].secretValue) == 40


def test_account_external(admin_user_client):
    account = admin_user_client.create_account(externalId='extid',
                                               externalIdType='extType')
    account = admin_user_client.wait_success(account)

    assert account.state == 'active'
    assert account.externalId == 'extid'
    assert account.externalIdType == 'extType'


def test_account_no_key(super_client):
    account = super_client.create_account(kind='admin')
    account = super_client.wait_success(account)
    creds = account.credentials()

    assert len(creds) >= 2

    account = super_client.create_account(kind='unknown')
    account = super_client.wait_success(account)
    creds = account.credentials()

    assert len(creds) == 0


def test_account_new_data(admin_user_client, super_client):
    user = admin_user_client.create_account(kind='user')
    user = admin_user_client.wait_success(user)

    assert user.state == 'active'
    assert super_client.reload(user).defaultNetworkId is None
    assert len(user.networks()) == 0

    account = admin_user_client.create_account(kind='project')
    account = admin_user_client.wait_success(account)

    assert account.state == 'active'
    assert super_client.reload(account).defaultNetworkId is not None

    networks = super_client.list_network(accountId=account.id)

    by_kind = {}

    for i in range(len(networks)):
        network = super_client.wait_success(networks[i])
        by_kind[networks[i].kind] = network
        assert network.state == 'active'

    assert len(networks) == 5
    assert len(by_kind) == 5

    assert 'dockerHost' in by_kind
    assert 'dockerNone' in by_kind
    assert 'dockerBridge' in by_kind
    assert 'hostOnlyNetwork' in by_kind
    assert 'dockerContainer' in by_kind

    network = by_kind['hostOnlyNetwork']

    assert network.id == super_client.reload(account).defaultNetworkId

    subnet = find_one(network.subnets)

    assert subnet.state == 'active'
    assert subnet.networkAddress == '10.42.0.0'
    assert subnet.cidrSize == 16
    assert subnet.gateway == '10.42.0.1'
    assert subnet.startAddress == '10.42.0.2'
    assert subnet.endAddress == '10.42.255.250'

    nsp = find_one(network.networkServiceProviders)
    nsp = super_client.wait_success(nsp)

    assert nsp.state == 'active'
    assert nsp.kind == 'agentInstanceProvider'

    service_by_kind = {}
    for service in nsp.networkServices():
        service = super_client.wait_success(service)
        service_by_kind[service.kind] = service

    assert len(nsp.networkServices()) == 6
    assert len(service_by_kind) == 6
    assert 'dnsService' in service_by_kind
    assert 'linkService' in service_by_kind
    assert 'ipsecTunnelService' in service_by_kind
    assert 'portService' in service_by_kind
    assert 'hostNatGatewayService' in service_by_kind
    assert 'healthCheckService' in service_by_kind


def test_account_context_create(new_context):
    assert new_context.client is not None
    assert new_context.user_client is not None
    assert new_context.project is not None
    assert new_context.account is not None

    assert len(new_context.user_client.list_project()) == 1


def test_account_purge(super_client, new_context):
    account_id = new_context.project.id
    account = new_context.project
    image_uuid = 'sim:{}'.format(random_num())
    host = new_context.host
    assert host.state == 'active'

    # Create another host
    host2 = register_simulated_host(new_context)
    assert host2.state == 'active'

    # create containers
    c1 = super_client.create_container(accountId=account_id,
                                       imageUuid=image_uuid,
                                       requestedHostId=host.id)
    c1 = super_client.wait_success(c1)
    assert c1.state == 'running'

    c2 = super_client.create_container(accountId=account_id,
                                       imageUuid=image_uuid,
                                       requestedHostId=host.id)
    c2 = super_client.wait_success(c2)
    assert c2.state == 'running'

    # create environment and services
    env = super_client. \
        create_environment(accountId=account_id,
                           name=random_str())
    env = super_client.wait_success(env)
    assert env.state == "active"

    launch_config = {"imageUuid": image_uuid}

    service1 = super_client.create_service(accountId=account_id,
                                           name=random_str(),
                                           environmentId=env.id,
                                           launchConfig=launch_config)
    service1 = super_client.wait_success(service1)
    assert service1.state == "inactive"

    service2 = super_client.create_service(accountId=account_id,
                                           name=random_str(),
                                           environmentId=env.id,
                                           launchConfig=launch_config)
    service2 = super_client.wait_success(service2)
    assert service2.state == "inactive"

    env.activateservices()
    service1 = super_client.wait_success(service1, 120)
    service2 = super_client.wait_success(service2, 120)
    assert service1.state == "active"
    assert service2.state == "active"

    account = super_client.reload(account)
    account = super_client.wait_success(account.deactivate())
    account = super_client.wait_success(account.remove())
    assert account.state == 'removed'
    assert account.removed is not None

    account = super_client.wait_success(account.purge())
    assert account.state == 'purged'

    host = super_client.wait_success(host)
    assert host.removed is not None
    assert host.state == 'purged'

    host2 = super_client.wait_success(host2)
    assert host2.removed is not None
    assert host2.state == 'purged'

    c1 = super_client.wait_success(c1)
    assert c1.removed is not None
    assert c1.state == 'removed'

    c2 = super_client.wait_success(c2)
    assert c2.removed is not None
    assert c2.state == 'removed'

    c1 = super_client.wait_success(c1.purge())
    assert c1.state == 'purged'

    volume = super_client.wait_success(c1.volumes()[0])
    assert volume.state == 'removed'

    volume = super_client.wait_success(volume.purge())
    assert volume.state == 'purged'

    service1 = super_client.wait_success(service1)
    assert service1.state == 'removed'

    service2 = super_client.wait_success(service2)
    assert service2.state == 'removed'

    env = super_client.wait_success(env)
    assert env.state == 'removed'
