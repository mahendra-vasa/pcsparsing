package com.freightliner.pcsparsing.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tools.ant.taskdefs.Tstamp;

import com.freightliner.pcsparsing.BOM;
import com.freightliner.pcsparsing.ComponentItem;
import com.freightliner.pcsparsing.PCSParsingException;
import com.freightliner.pcsparsing.SerialNumber;
import com.freightliner.pcsparsing.Module;

/**
 * The class provides the finder service for Item by forming the HQL queries.
 * 
 * @author jftl8v
 * 
 */
public class ItemFinder extends Finder {

	protected static final Log log = LogFactory.getLog(ItemFinder.class);

   /**
    * Find the component item according to the item number.
    * 
    * @param String number: the item number
    * @return ComponentItem	the component item
    */
	// @TODO: Should require BOM and module or not?
	public static ComponentItem find(String number)
		throws PCSParsingException {
		ComponentItem item = null;
		Session session = null;
		try {
			session = getSession();
			Query query =
				session.createQuery(
					"from ComponentItemImpl item "
						+ " where item.number = :number ");
			query.setString("number", StringUtils.rightPad(number, 25));
			item = (ComponentItem) query.uniqueResult();
		} catch (HibernateException e) {
			throw new PCSParsingException(e);
		} finally {
			close(session);
		}

		return item;
	}

   /**
    * Find the items that has been ordered according to the serial number and the module numbers.
    * 
    * @param SerialNumber serialNumber: the serial number
    * @param Collection moduleNumbers: the module numbers
    * @return Collection	the collection of the items 
    */
	public static Collection findOrderedItems(SerialNumber serialNumber, Collection moduleNumbers)
		throws PCSParsingException {
		//Collection moduleNumbers = findModules(serialNumber);
		Collection boms = findBOMs(serialNumber, moduleNumbers);
		Collection items = findItems(serialNumber, boms);

		Collection orderedItems = new ArrayList();
		orderedItems.addAll(boms);
		orderedItems.addAll(items);
		return orderedItems;
	}

   /**
    * Find a collection of modules according to the serial number.
    * 
    * @param SerialNumber serialNumber: the serial number
    * @return Collection	a collection of modules
    * 
    * @throws PCSParsingException	if there's HibernateException
    */
	public static Collection findModules(SerialNumber serialNumber)
		throws PCSParsingException {
		Query query = null;
		Session session = null;
		try {
			session = getSession();

			// Find ordered modules
			String queryString = "from ModuleImpl module "
					+ " where module.compID.serialNumber = :serialNumber "
					+ " and exists(from ModuleImpl m join module.order)";
			query = session.createQuery(queryString);
			query.setString("serialNumber", serialNumber.getNumber());
			Collection modules = new ArrayList();
			for (Iterator iter = query.list().iterator(); iter.hasNext();) {
				Object moduleObject = iter.next();
				modules.add(moduleObject);
			}
			return modules;
		} catch (HibernateException e) {
			throw new PCSParsingException(e);
		} finally {
			close(session);
		}

	}

   /**
    * Find the BOMs according to the serial number and the modules.
    * 
    * @param SerialNumber serialNumber: the serial number
    * @param Collection moduleCollections: the collections of the modules
    * @return Collection	the collection of BOMs
    * 
    * @throws PCSParsingException	if there's HibernateException
    */
	private static Collection findBOMs(SerialNumber serialNumber, Collection moduleCollections) 
			throws PCSParsingException {
		
		Collection boms = new ArrayList();
		String maxTSO = getMaxTSOSequenceNumber(serialNumber.getNumber());
		for (Iterator iter = moduleCollections.iterator(); iter.hasNext();) {
			Module module = (Module) iter.next();
			Query query = null;
			Session session = null;
			try {
				session = getSession();
				String queryString =
					"from BOMImpl bom "
						+ " where bom.compID.serialNumber = :serialNumber "
						+ " and bom.compID.moduleNumber = :moduleNumber "
						+ " and bom.compID.tsoSequenceNumber = :tsoSequenceNumber ";
				query = session.createQuery(queryString);
				query.setString("serialNumber", serialNumber.getNumber());
				query.setString("moduleNumber", module.getNumber());
				query.setString("tsoSequenceNumber", maxTSO);
				boms.addAll(query.list());
			} catch (HibernateException e) {
				throw new PCSParsingException("Could not find BOMS for " + 
						serialNumber + " " + module.getNumber() + " " + maxTSO, e);
			} finally {
				close(session);
			}
		}
		return boms;
	}

   /**
    * Find the items according to the serial number and the collection of BOMs
    * 
    * @param SerialNumber serialNumber: the serial number
    * @param Collection boms: the BOMs
    * @return Collection	the collection of the component items
    * 
    * @throws PCSParsingException	if there's HibernateException
    */
	private static Collection findItems(SerialNumber serialNumber, Collection boms) 
				throws PCSParsingException {
		Collection items = new HashSet();
		String maxTSO = getMaxTSOSequenceNumber(serialNumber.getNumber());
		for (Iterator iter = boms.iterator(); iter.hasNext();) {
			BOM bom = (BOM) iter.next();
			ItemFinder.findWithLocations("");
			Query query = null;
			Session session = null;
			try {
				session = getSession();
				String queryString =
					"from ComponentItemImpl item "
						+ " left outer join fetch item.locations "
						+ " where item.bom.compID.serialNumber = :serialNumber "
						+ " and item.bom.compID.moduleNumber = :moduleNumber "
						+ " and item.bom.compID.number = :bomNumber "
						+ " and item.bom.compID.tsoSequenceNumber = :tsoSequenceNumber ";
				query = session.createQuery(queryString);
				query.setString("serialNumber", serialNumber.getNumber());
				query.setString("moduleNumber", bom.getModuleNumber());
				query.setString("bomNumber", bom.getNumber());
				query.setString("tsoSequenceNumber", maxTSO);
				items.addAll(query.list());
			} catch (HibernateException e) {
				throw new PCSParsingException(e);
			} finally {
				close(session);
			}
		}
		return items;
	}

   /**
    * Find the item and its locations by the item number.
    * 
    * @param String number: the item number
    * @return ComponentItem	the component item
    * @throws PCSParsingException	if there's HibernateException
    */
	public static ComponentItem findWithLocations(String number)
		throws PCSParsingException {
		ComponentItem item = null;
		Session session = null;
		try {
			session = getSession();
			Query query =
				session.createQuery(
					"from ComponentItemImpl item "
						+ " left outer join fetch item.locations "
						+ " where item.number = :number ");
			query.setString("number", StringUtils.rightPad(number, 25));
			item = (ComponentItem) query.uniqueResult();
		} catch (HibernateException e) {
			throw new PCSParsingException(e);
		} finally {
			close(session);
		}

		return item;
	}

}
