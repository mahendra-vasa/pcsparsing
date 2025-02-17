package com.freightliner.pcsparsing.service;

import java.util.*;

import net.sf.hibernate.*;

import org.apache.commons.logging.*;

import com.freightliner.pcsparsing.*;
import com.freightliner.pcsparsing.impl.ModuleImpl;

/**
 * The class provide the finder service for BOM by forming the HQL queries.
 * 
 * @author jftl8v
 * 
 */
public class ModuleFinder extends Finder {

    private static final Log log = LogFactory.getLog(ModuleFinder.class);

    /**
     * Find the ordered truck modules.
     * 
     * @param truck
     * @return truck's modules, ordered by module number ascending
     * @throws IllegalArgumentException
     *           if truck is null
     */
    public static final List getModulesOrderedByNumber(Truck truck)
            throws PCSParsingException {
        if (truck == null) { 
        	throw new IllegalArgumentException(
                "Argument truck must not be null"); }

        String serialNumber = truck.getSerialNumber();
        return getModulesOrderedByNumber(serialNumber);
    }

    /**
     * Find the ordered modules belongedto a specific truck serial number.
     * 
     * @param truckSerialNumber
     * @return truck's modules, ordered by module number ascending. Empty list
     *         if truck does not exist
     * @throws PCSParsingException
     */
    public static final List getModulesOrderedByNumber(String truckSerialNumber)
            throws PCSParsingException {
        return getModulesOrderedByNumber(truckSerialNumber,
                IsOrderedSearchCriteria.ALL, DepartmentSearchCriteria.ALL,
                HasCWOSearchCriteria.ALL);
    }

   /**
    * Find the modules according to the serial number and the module number.
    * 
    * @param String serialNumber: the serial number
    * @param String number: the module number
    * @return List	a list of modules
    * 
    * @throws PCSParsingException	if there is a HibernateException
    */
	public static List find(String serialNumber, String number)
            throws PCSParsingException {
        List moduleList = new ArrayList();
        Session session = null;
        try {
            session = getSession();
            Query query = session
                    .createQuery("from ModuleImpl as module where module.compID.number = :number and module.compID.serialNumber = :serialNumber");
            query.setString("number", number);
            query.setString("serialNumber", serialNumber);            
            moduleList = query.list();
            
        } catch (HibernateException e) {
            throw new PCSParsingException(e);
        } finally {
            close(session);
        }

        return moduleList;
    }
    
   /**
    * Find the modules based on the serial number and various search criteria.
    * 
    * @param String truckSerialNumber: the serial number
    * @param IsOrderedSearchCriteria isOrdered: the is-ordered search criterion
    * @param DepartmentSearchCriteria department: the department search criterion
    * @param HasCWOSearchCriteria hasCWO: the has-CWO search criterion
    * @return List	a list of modules
    * 
    * @throws PCSParsingException	if there is HibernateException
    */
    public static List getModulesOrderedByNumber(String truckSerialNumber,
            IsOrderedSearchCriteria isOrdered,
            DepartmentSearchCriteria department, 
            HasCWOSearchCriteria hasCWO)
            throws PCSParsingException {

        if (truckSerialNumber == null || truckSerialNumber.equals("")) { return Collections.EMPTY_LIST; }

        String fromClause = " from ModuleImpl module "
                + " left join fetch module.departmentCodes ";
        String whereClause = " where module.compID.serialNumber = :serialNumber ";
        if (department != DepartmentSearchCriteria.ALL) {
            whereClause += " and :department in elements(module.departmentCodes)";
        }

        if (isOrdered != IsOrderedSearchCriteria.ALL) {
        	
            if (!isOrdered.booleanValue()) {
                fromClause += " left join fetch module.order as ord ";
                whereClause += "  and not exists(from ModuleImpl m join module.order) ";
            } else {
                fromClause += " left join fetch module.order as ord ";
                whereClause += "  and exists(from ModuleImpl m join module.order) ";
            }
            if (log.isDebugEnabled())
                    log.debug("isOrdered: " + isOrdered.booleanValue());
        } else {
            fromClause += " left join fetch module.order as ord ";
        }

        if (hasCWO != HasCWOSearchCriteria.ALL) {
            fromClause += " left join module.boms as bom ";
            if (hasCWO.booleanValue()) {
                whereClause += "  and bom.cwoIndicator = 'C' ";
            } else {
                whereClause += "  and not exists(from ModuleImpl m left join m.boms as b where m.compID.serialNumber = :serialNumber "
                        + "and b.cwoIndicator = 'C' and m.compID.number = module.compID.number) ";
                  //      + "and m.compID.sequenceNumber = module.compID.sequenceNumber)";
            }
            if (log.isDebugEnabled())
                    log.debug("HasCWOSearchCriteria: " + hasCWO.booleanValue());
        }

        Query query = null;
        Session session = null;
        try {
            session = getSession();
            query = session.createQuery("select module " + fromClause
                    + whereClause);            
            query.setString("serialNumber", truckSerialNumber);
            if (log.isDebugEnabled()) {
                log.debug(query.getQueryString());
                log.debug("serialNumber: '" + truckSerialNumber + "'");
            }
            if (department != DepartmentSearchCriteria.ALL) {
                // TODO: There is probably a more elegant solution here
                query.setString("department", department.getKey());
                if (log.isDebugEnabled())
                        log.debug("department: " + department.getKey());
            }
            query.setCacheable(true);
            if (log.isDebugEnabled()) log.debug("Starting query");
            // DB2 'order by' doesn't work
            SortedMap modules = new TreeMap();
            String lastNumber = null;
            for (Iterator iter = query.list().iterator(); iter.hasNext();) {
                Module module = (Module) iter.next();
                String number = module.getNumber();
                if (!number.equals(lastNumber)) {
                    modules.put(number, module);
                    lastNumber = number;
                }
            }

            if (log.isDebugEnabled())log.debug("populateIsRevisionLevelChanged");
            populateIsRevisionLevelChanged(session, modules, truckSerialNumber);
            if (log.isDebugEnabled()) log.debug("populateInvalidStatus");
            populateInvalidStatus(session, modules, truckSerialNumber);
 
            if (log.isDebugEnabled()) log.debug("making List");
            List list = new ArrayList(modules.values());
            if (log.isDebugEnabled()) log.debug("done");            
           
            return list;

        } catch (HibernateException e) {
            throw new PCSParsingException("Could not get modules for truck: "
                    + truckSerialNumber, e);
        } finally {
            close(session);
        }
    }

   /**
    * Update the revision level.
    * 
    * @param Session session: the Hibernate session
    * @param Map modules: the modules
    * @param String serialNumber: the serial number
    * @throws HibernateException
    * 
    */
    private static void populateIsRevisionLevelChanged(Session session,
            Map modules, String serialNumber) throws HibernateException {
        Query query = session
                .createQuery("select module.compID.number from ModuleImpl module "
                        + " left join module.boms as bom "
                        + " left join bom.items as item "
                        + "where module.compID.serialNumber = :serialNumber and "
                        + " item.engRevisionLevel <> item.mfgRevisionLevel");
        query.setCacheable(true);
       query.setString("serialNumber", serialNumber);

        for (Iterator iter = query.iterate(); iter.hasNext();) {
            String number = (String) iter.next();
            ModuleImpl module = (ModuleImpl) modules.get(number);
            if (module != null) {
                module.setRevisionLevelUpdated(true);
            }
        }
    }

   /**
    * Set the module status as invalid.
    * @param Session session: the Hibernate session
    * @param Map modules: the modules
    * @param String serialNumber: the serial number
    * 
    * @throws HibernateException
    */
    private static void populateInvalidStatus(Session session, Map modules,
            String serialNumber) throws HibernateException {
        // Could improve speed by looking only for ids?
        Query query = session
                .createQuery("select module.compID.number from ModuleImpl module "
                        + " left join module.boms as bom "
                        + " left join bom.items as item "
                        + "where module.compID.serialNumber = :serialNumber and "
                        + " item.status IN ('D', 'S', 'O')");
        query.setCacheable(true);
        query.setString("serialNumber", serialNumber);

        for (Iterator iter = query.iterate(); iter.hasNext();) {
            String number = (String) iter.next();
            ModuleImpl module = (ModuleImpl) modules.get(number);
            if (module != null) {
                module.setStatusValid(false);
            }
        }
    }

}
