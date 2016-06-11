package io.cattle.platform.configitem.server.agentinclude.impl;

import org.junit.Test;

import static org.junit.Assert.*;

public class AgentIncludeMapImplTest {
    @Test
    public void sanitize() throws Exception {
        assertEquals("pyagent", AgentIncludeMapImpl.sanitize("pyagent_arm"));
        assertEquals("pyagent", AgentIncludeMapImpl.sanitize("pyagent"));
        assertEquals("node.services", AgentIncludeMapImpl.sanitize("node-services_arm"));
        assertEquals("node.services", AgentIncludeMapImpl.sanitize("node-services"));
    }

    @Test
    public void inferArch() throws Exception {
        assertEquals("arm", AgentIncludeMapImpl.inferArch("pyagent_arm"));
        assertEquals("", AgentIncludeMapImpl.inferArch("pyagent"));
        assertEquals("arm", AgentIncludeMapImpl.inferArch("node-services_arm"));
        assertEquals("", AgentIncludeMapImpl.inferArch("node-services"));
    }

}