package com.musicbox.server.db;

import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Created with IntelliJ IDEA.
 * User: giko
 * Date: 3/8/13
 * Time: 1:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class Connection {
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("server");

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public static Session getSession() {
        return (Session) getEntityManager().getDelegate();
    }

    public static Session getSessionByEntityManager(EntityManager entityManager) {
        return (Session) entityManager.getDelegate();
    }

}
