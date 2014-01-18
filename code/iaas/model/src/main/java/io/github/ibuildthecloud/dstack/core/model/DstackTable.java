/**
 * This class is generated by jOOQ
 */
package io.github.ibuildthecloud.dstack.core.model;

/**
 * This class is generated by jOOQ.
 */
@javax.annotation.Generated(value    = { "http://www.jooq.org", "3.3.0" },
                            comments = "This class is generated by jOOQ")
@java.lang.SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class DstackTable extends org.jooq.impl.SchemaImpl {

	private static final long serialVersionUID = 1773556793;

	/**
	 * The singleton instance of <code>dstack</code>
	 */
	public static final DstackTable DSTACK = new DstackTable();

	/**
	 * No further instances allowed
	 */
	private DstackTable() {
		super("dstack");
	}

	@Override
	public final java.util.List<org.jooq.Table<?>> getTables() {
		java.util.List result = new java.util.ArrayList();
		result.addAll(getTables0());
		return result;
	}

	private final java.util.List<org.jooq.Table<?>> getTables0() {
		return java.util.Arrays.<org.jooq.Table<?>>asList(
			io.github.ibuildthecloud.dstack.core.model.tables.AccountTable.ACCOUNT,
			io.github.ibuildthecloud.dstack.core.model.tables.AgentTable.AGENT,
			io.github.ibuildthecloud.dstack.core.model.tables.AgentGroupTable.AGENT_GROUP,
			io.github.ibuildthecloud.dstack.core.model.tables.ChangelogLockTable.CHANGELOG_LOCK,
			io.github.ibuildthecloud.dstack.core.model.tables.ConfigItemTable.CONFIG_ITEM,
			io.github.ibuildthecloud.dstack.core.model.tables.ConfigItemStatusTable.CONFIG_ITEM_STATUS,
			io.github.ibuildthecloud.dstack.core.model.tables.CoreChangelogTable.CORE_CHANGELOG,
			io.github.ibuildthecloud.dstack.core.model.tables.CredentialTable.CREDENTIAL,
			io.github.ibuildthecloud.dstack.core.model.tables.DataTable.DATA,
			io.github.ibuildthecloud.dstack.core.model.tables.DatabasechangelogTable.DATABASECHANGELOG,
			io.github.ibuildthecloud.dstack.core.model.tables.DatabasechangeloglockTable.DATABASECHANGELOGLOCK,
			io.github.ibuildthecloud.dstack.core.model.tables.HostTable.HOST,
			io.github.ibuildthecloud.dstack.core.model.tables.ImageTable.IMAGE,
			io.github.ibuildthecloud.dstack.core.model.tables.ImageFormatTable.IMAGE_FORMAT,
			io.github.ibuildthecloud.dstack.core.model.tables.ImageStoragePoolMapTable.IMAGE_STORAGE_POOL_MAP,
			io.github.ibuildthecloud.dstack.core.model.tables.InstanceTable.INSTANCE,
			io.github.ibuildthecloud.dstack.core.model.tables.InstanceHostMapTable.INSTANCE_HOST_MAP,
			io.github.ibuildthecloud.dstack.core.model.tables.IpAddressTable.IP_ADDRESS,
			io.github.ibuildthecloud.dstack.core.model.tables.IpAddressNicMapTable.IP_ADDRESS_NIC_MAP,
			io.github.ibuildthecloud.dstack.core.model.tables.ItemPoolTable.ITEM_POOL,
			io.github.ibuildthecloud.dstack.core.model.tables.ItemPoolGeneratorTable.ITEM_POOL_GENERATOR,
			io.github.ibuildthecloud.dstack.core.model.tables.NetworkTable.NETWORK,
			io.github.ibuildthecloud.dstack.core.model.tables.NicTable.NIC,
			io.github.ibuildthecloud.dstack.core.model.tables.OfferingTable.OFFERING,
			io.github.ibuildthecloud.dstack.core.model.tables.ProcessInstanceTable.PROCESS_INSTANCE,
			io.github.ibuildthecloud.dstack.core.model.tables.SettingTable.SETTING,
			io.github.ibuildthecloud.dstack.core.model.tables.StoragePoolTable.STORAGE_POOL,
			io.github.ibuildthecloud.dstack.core.model.tables.StoragePoolHostMapTable.STORAGE_POOL_HOST_MAP,
			io.github.ibuildthecloud.dstack.core.model.tables.VnetTable.VNET,
			io.github.ibuildthecloud.dstack.core.model.tables.VolumeTable.VOLUME,
			io.github.ibuildthecloud.dstack.core.model.tables.VolumeStoragePoolMapTable.VOLUME_STORAGE_POOL_MAP,
			io.github.ibuildthecloud.dstack.core.model.tables.ZoneTable.ZONE);
	}
}
