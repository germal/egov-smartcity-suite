/* eGov suite of products aim to improve the internal efficiency,transparency,
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
package org.egov.wtms.web.controller.reports;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.egov.commons.EgwStatus;
import org.egov.infra.admin.master.entity.User;
import org.egov.wtms.application.service.DailyWTCollectionReport;
import org.egov.wtms.application.service.DailyWTCollectionReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Controller
@RequestMapping("/report/dailyWTCollectionReport/search")
public class DailyWTCollectionReportController {

    @Autowired
    public DailyWTCollectionReportService dailyWTCollectionReportService;

    @ModelAttribute
    public DailyWTCollectionReport reportModel() {
        return new DailyWTCollectionReport();
    }

    @ModelAttribute("collectionMode")
    public Map<String, String> loadInstrumentTypes() {
        return dailyWTCollectionReportService.getCollectionModeMap();
    }

    @ModelAttribute("operators")
    public Set<User> loadCollectionOperators() {
        return dailyWTCollectionReportService.getUsers();
    }

    @ModelAttribute("status")
    public List<EgwStatus> loadStatus() {
        return dailyWTCollectionReportService.getStatusByModule();
    }

    @RequestMapping(method = GET)
    public String search(final Model model) {
        model.addAttribute("currentDate", new Date());
        return "dailyWTCollection-search";
    }

    @RequestMapping(value = "/result", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody void searchResult(@RequestParam final Date fromDate,
            @RequestParam final Date toDate, final HttpServletRequest request, final HttpServletResponse response)
                    throws IOException, ParseException {
        String collectionMode = "";
        String collectionOperator = "";
        String status = "";
        if (null != request.getParameter("collectionMode"))
            collectionMode = request.getParameter("collectionMode");
        if (null != request.getParameter("collectionOperator"))
            collectionOperator = request.getParameter("collectionOperator");
        if (null != request.getParameter("status"))
            status = request.getParameter("status");

        final List<DailyWTCollectionReport> collectionDetailsList = dailyWTCollectionReportService.getCollectionDetails(fromDate,
                toDate, collectionMode, collectionOperator, status);
        final String result = new StringBuilder("{ \"data\":").append(toJSON(collectionDetailsList)).append("}")
                .toString();
        IOUtils.write(result, response.getWriter());
    }

    private Object toJSON(final Object object) {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.registerTypeAdapter(DailyWTCollectionReport.class,
                new DailyWTCollectionAdaptor()).create();
        final String json = gson.toJson(object);
        return json;
    }
}
