package io.cattle.platform.configitem.server.agentinclude.impl;

import io.cattle.platform.archaius.util.ArchaiusUtil;
import io.cattle.platform.configitem.server.agentinclude.AgentIncludeMap;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import org.apache.commons.codec.binary.Hex;

import com.netflix.config.DynamicListProperty;

public class AgentIncludeMapImpl implements AgentIncludeMap {

    private static final DynamicListProperty<String> KEYS = ArchaiusUtil.getList("agent.packages.types");

    @Override
    public List<String> getNamedMaps() {
        return KEYS.get();
    }

    protected static String sanitize(String s) {
        if (s == null) {
            return s;
        }

        if (s.contains("_")) {
            s = s.substring(0, s.indexOf('_'));
        }

        return s.replaceAll("-", ".").toLowerCase();
    }

    protected static String inferArch(String s) {
        if (s.contains("_")) {
            return s.substring(s.indexOf('_') + 1);
        }
        return "";
    }

    @Override
    public Map<String, String> getMap(String name) {
        Map<String, String> result = new LinkedHashMap<String, String>();

        if (name == null) {
            return result;
        }

        String arch = inferArch(name);

        for (String item : ArchaiusUtil.getList("agent.packages." + sanitize(name)).get()) {
            String key = String.format("agent.package.%s.url", sanitize(item));

            String value = null;
            if (arch.length() > 0) {
                String archValue = ArchaiusUtil.getString(key + "." + arch).get();
                if (archValue != null) {
                    value = archValue;
                }
            } else {
                value = ArchaiusUtil.getString(key).get();
            }

            if (value != null) {
                result.put(item, value);
            }
        }

        return result;
    }

    @Override
    public String getSourceRevision(String name) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            for (Map.Entry<String, String> entry : getMap(name).entrySet()) {
                md.update(entry.getKey().getBytes("UTF-8"));
                md.update(entry.getValue().getBytes("UTF-8"));
            }

            return Hex.encodeHexString(md.digest());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}