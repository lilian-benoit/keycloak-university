package org.fr.devoxx.demo;

import org.keycloak.util.ldap.LDAPEmbeddedServer;

import java.util.Properties;

public class LDAPServer {

    public static void main(String[] args) throws Exception {
        Properties defaultProperties = new Properties();
        defaultProperties.load(LDAPServer.class.getResourceAsStream("/ldap.properties"));
        LDAPEmbeddedServer.execute(args, defaultProperties);
    }
}
