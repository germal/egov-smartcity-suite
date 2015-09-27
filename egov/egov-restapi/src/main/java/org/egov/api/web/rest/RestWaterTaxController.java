/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2015>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

	1) All versions of this program, verbatim or modified must carry this
	   Legal Notice.

	2) Any misrepresentation of the origin of the material is prohibited. It
	   is required that all modified versions of this material be marked in
	   reasonable ways as different from the original version.

	3) This license does not grant any rights to any user of the program
	   with regards to rights under trademark law for use of the trade names
	   or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.api.web.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.egov.wtms.application.rest.WaterTaxDue;
import org.egov.wtms.application.service.ConnectionDemandService;
import org.egov.wtms.masters.entity.ConnectionCategory;
import org.egov.wtms.masters.entity.DocumentNames;
import org.egov.wtms.masters.entity.PipeSize;
import org.egov.wtms.masters.entity.PropertyType;
import org.egov.wtms.masters.entity.UsageType;
import org.egov.wtms.masters.entity.WaterSource;
import org.egov.wtms.masters.entity.enums.ConnectionType;
import org.egov.wtms.masters.service.ConnectionCategoryService;
import org.egov.wtms.masters.service.DocumentNamesService;
import org.egov.wtms.masters.service.PipeSizeService;
import org.egov.wtms.masters.service.PropertyTypeService;
import org.egov.wtms.masters.service.UsageTypeService;
import org.egov.wtms.masters.service.WaterSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestWaterTaxController {

    @Autowired
    private ConnectionDemandService connectionDemandService;
    
    @Autowired
    private ConnectionCategoryService connectionCategoryService;
    
    @Autowired
    private UsageTypeService usageTypeService;
    
    @Autowired
    private DocumentNamesService documentNamesService;
    
    @Autowired
    private PipeSizeService pipeSizeService;
    
    @Autowired
    private WaterSourceService waterSourceService;
    
    @Autowired
    private PropertyTypeService propertyTypeService;

    /*
     * Returns Total tax due for the water connection for a given ConsumerCode
     */
    @RequestMapping(value = "/watercharges/due/bycode/{consumerCode}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public WaterTaxDue getWaterTaxDueByConsumerCode(@PathVariable final String consumerCode)
            throws JsonGenerationException, JsonMappingException, IOException {
        return connectionDemandService.getDueDetailsByConsumerCode(consumerCode);

    }

    /*
     * Returns Total tax due for list of water connections for a given PropertyIdentifier
     */
    @RequestMapping(value = {
    "/watercharges/due/byptno/{assessmentNumber}" }, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public WaterTaxDue getWaterTaxDueByPropertyId(@PathVariable final String assessmentNumber)
            throws JsonGenerationException, JsonMappingException, IOException {
        return connectionDemandService.getDueDetailsByPropertyId(assessmentNumber);

    }
    
    @RequestMapping(value = "/watercharges/categories", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getConnectionCategoryList() throws JsonGenerationException, JsonMappingException, IOException {
        List<ConnectionCategory> connectionCategoryList = connectionCategoryService.getConnectionCategoryListForRest();
        return getJSONResponse(connectionCategoryList);
    }
    
    @RequestMapping(value = "/watercharges/usagetypes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getUsageTypeList() throws JsonGenerationException, JsonMappingException, IOException {
        List<UsageType> usageTypeList = usageTypeService.getUsageTypeListForRest();
        return getJSONResponse(usageTypeList);
    }
    
    @RequestMapping(value = "/watercharges/documentnames", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getDocumentNameList() throws JsonGenerationException, JsonMappingException, IOException {
        List<DocumentNames> documentNamesList = documentNamesService.getDocumentNamesListForRest();
        return getJSONResponse(documentNamesList);
    }
    
    @RequestMapping(value = "/watercharges/pipesizes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getPipeSizeList() throws JsonGenerationException, JsonMappingException, IOException {
        List<PipeSize> pipeSizeList = pipeSizeService.getPipeSizeListForRest();
        return getJSONResponse(pipeSizeList);
    }
    
    @RequestMapping(value = "/watercharges/watersourcetypes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getWaterSourceTypes() throws JsonGenerationException, JsonMappingException, IOException {
        List<WaterSource> waterSourceList = waterSourceService.getWaterSourceListForRest();
        return getJSONResponse(waterSourceList);
    }
    
    @RequestMapping(value = "/watercharges/waterconnectiontypes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getWaterConnectionTypes() throws JsonGenerationException, JsonMappingException, IOException {
        List<String> connectionTypeList = new ArrayList<String>(0);
        connectionTypeList.add(ConnectionType.METERED.name());
        connectionTypeList.add(ConnectionType.NON_METERED.name());
        return getJSONResponse(connectionTypeList);
    }
    
    @RequestMapping(value = "/watercharges/propertytypes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getPropertyTypes() throws JsonGenerationException, JsonMappingException, IOException {
        List<PropertyType> propertyTypeList = propertyTypeService.getPropertyTypeListForRest();
        return getJSONResponse(propertyTypeList);
    }
    
    /**
     * This method is used to prepare jSON response.
     * 
     * @param obj - a POJO object
     * @return jsonResponse - JSON response string
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    private String getJSONResponse(Object obj) throws JsonGenerationException, JsonMappingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(JsonMethod.FIELD, Visibility.ANY);
        String jsonResponse = objectMapper.writeValueAsString(obj);
        return jsonResponse;
    }

}
