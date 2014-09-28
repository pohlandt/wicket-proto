package com.pohlandt;

import java.io.IOException;
import javax.persistence.*;
import javax.servlet.*;

public class JpaServletFilter implements Filter
{
   private static final ThreadLocal<EntityManager> entityManagerHolder = new ThreadLocal<EntityManager>();
   private EntityManagerFactory emf;

   @Override public void init( FilterConfig filterConfig ) throws ServletException
   {
	   try{
		   String puName = filterConfig.getServletContext().getInitParameter( "jpa-persistence-unit-name" );
		   System.out.println("persistence unit name: " + puName);
		   emf = Persistence.createEntityManagerFactory( puName );   
	   } catch (Exception ex){
		   System.out.println("failed to create EntityManagerFactory");
		   System.out.println(ex);
	   }
   }

   @Override public void destroy()
   {
      emf.close();
      emf = null;
   }

   @Override public void doFilter( ServletRequest requ, ServletResponse resp, FilterChain chain )
   throws IOException, ServletException
   {
	  try {
		  EntityManager     em = emf.createEntityManager();
	      EntityTransaction tx = em.getTransaction();
	      entityManagerHolder.set( em );
	      try {
	         tx.begin();
	         chain.doFilter( requ, resp );
	         tx.commit();
	      } finally {
	         if( tx != null && tx.isActive() ) tx.rollback();
	         em.close();
	         entityManagerHolder.remove();
	      }		  
	  }
	  catch(Exception ex){
		  System.out.println("failure in JpaServletFilter.doFilter");
		  System.out.println(ex);
	  }
   }

   public static EntityManager getEntityManager()
   {
      return entityManagerHolder.get();
   }
}