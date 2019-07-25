/*
 * eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) <2017>  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *      Further, all user interfaces, including but not limited to citizen facing interfaces,
 *         Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *         derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *      For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *      For any further queries on attribution, including queries on brand guidelines,
 *         please contact contact@egovernments.org
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.bpa.web.controller.transaction;

import static org.egov.bpa.utils.BpaConstants.BOUNDARY_TYPE_CITY;
import static org.egov.bpa.utils.BpaConstants.FILESTORE_MODULECODE;
import static org.egov.bpa.utils.BpaConstants.REVENUE_HIERARCHY_TYPE;
import static org.egov.bpa.utils.BpaConstants.WARD;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.egov.bpa.transaction.entity.BpaApplication;
import org.egov.bpa.transaction.entity.dto.SearchBpaApplicationForm;
import org.egov.bpa.transaction.service.InspectionService;
import org.egov.bpa.transaction.service.LettertoPartyService;
import org.egov.bpa.transaction.service.SearchBpaApplicationService;
import org.egov.bpa.web.controller.adaptor.SearchBpaApplicationAdaptor;
import org.egov.eis.entity.Employee;
import org.egov.eis.entity.Jurisdiction;
import org.egov.eis.service.EmployeeService;
import org.egov.infra.admin.master.entity.Boundary;
import org.egov.infra.admin.master.entity.BoundaryType;
import org.egov.infra.admin.master.service.BoundaryTypeService;
import org.egov.infra.admin.master.service.CrossHierarchyService;
import org.egov.infra.web.support.ui.DataTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/application")
public class SearchBpaApplicationController extends BpaGenericApplicationController {

    private static final String SEARCH_BPA_APPLICATION_FORM = "searchBpaApplicationForm";
    private static final String APPLICATION_HISTORY = "applicationHistory";

    @Autowired
    private SearchBpaApplicationService searchBpaApplicationService;
    @Autowired
    private InspectionService inspectionService;
    @Autowired
    private LettertoPartyService lettertoPartyService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private BoundaryTypeService boundaryTypeService;
    @Autowired
    private CrossHierarchyService crossHierarchyService;

    @GetMapping("/search")
    public String search(final Model model) {
        prepareFormData(model);
        model.addAttribute(SEARCH_BPA_APPLICATION_FORM, new SearchBpaApplicationForm());
        return "search-bpa-application";
    }

    @PostMapping(value = "/search", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String searchApplications(@Valid @ModelAttribute final SearchBpaApplicationForm searchBpaApplicationForm) {
        return new DataTable<>(searchBpaApplicationService.pagedSearch(searchBpaApplicationForm),
                searchBpaApplicationForm.draw())
                        .toJson(SearchBpaApplicationAdaptor.class);
    }

    @GetMapping("/view/{applicationNumber}")
    public String viewApplicationForm(final Model model, @PathVariable final String applicationNumber,
            final HttpServletRequest request) {
        BpaApplication application = applicationBpaService.findByApplicationNumber(applicationNumber);
        /*
         * for (DCRDocument dcrDocument : application.getDcrDocuments()) { for (StoreDCRFiles file :
         * dcrDocument.getDcrAttachments()) { bpaUtils.addQrCodeToPdfDocuments(file.getFileStoreMapper(), application);
         * pdfQrCodeAppendService.addStamp(file.getFileStoreMapper(),application); } }
         */
        model.addAttribute("bpaApplication", application);
        model.addAttribute("citizenOrBusinessUser", bpaUtils.logedInuseCitizenOrBusinessUser());
        model.addAttribute(APPLICATION_HISTORY,
                workflowHistoryService.getHistory(application.getAppointmentSchedule(), application.getCurrentState(),
                        application.getStateHistory()));
        model.addAttribute("inspectionList", inspectionService.findByBpaApplicationOrderByIdAsc(application));
        model.addAttribute("lettertopartylist", lettertoPartyService.findByBpaApplicationOrderByIdDesc(application));
        buildReceiptDetails(application.getDemand().getEgDemandDetails(), application.getReceipts());
        return "viewapplication-form";
    }

    @GetMapping("/downloadfile/{fileStoreId}")
    public ResponseEntity<InputStreamResource> download(@PathVariable final String fileStoreId) {
        return fileStoreUtils.fileAsResponseEntity(fileStoreId, FILESTORE_MODULECODE, true);
    }

    @GetMapping("/bpacollectfee")
    public String showCollectionPendingRecords(final Model model) {
        prepareFormData(model);
        model.addAttribute(SEARCH_BPA_APPLICATION_FORM, new SearchBpaApplicationForm());
        return "search-collect-fee";
    }

    @PostMapping(value = "/bpacollectfee", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String searchCollectionPendingRecords(@Valid @ModelAttribute final SearchBpaApplicationForm searchBpaApplicationForm) {
        return new DataTable<>(searchBpaApplicationService.hasFeeCollectionPending(searchBpaApplicationForm),
                searchBpaApplicationForm.draw())
                        .toJson(SearchBpaApplicationAdaptor.class);
    }

    @GetMapping("/bpadocumentscrutiny")
    public String showDocumentScrutinyPendingRecords(final Model model) {
        List<Boundary> employeeMappedZone = new ArrayList<>();
        List<Boundary> mappedElectionWard = new ArrayList<>();
        Set<Boundary> electionWards = new HashSet<>();
        BoundaryType revenueType = boundaryTypeService.getBoundaryTypeByNameAndHierarchyTypeName(WARD, REVENUE_HIERARCHY_TYPE);
        final Employee employee = employeeService.getEmployeeById(securityUtils.getCurrentUser().getId());
        for (Jurisdiction jurisdiction : employee.getJurisdictions())
            if (!BOUNDARY_TYPE_CITY.equals(jurisdiction.getBoundaryType().getName())) {
                mappedElectionWard.add(jurisdiction.getBoundary());
                break;
            }
        List<Boundary> revenueWards = crossHierarchyService
                .getParentBoundaryByChildBoundaryAndParentBoundaryType(mappedElectionWard.get(0).getId(), revenueType.getId());
        employeeMappedZone.add(revenueWards.get(0).getParent());
        model.addAttribute(SEARCH_BPA_APPLICATION_FORM, new SearchBpaApplicationForm());
        List<Boundary> revWards = boundaryService.getActiveChildBoundariesByBoundaryId(revenueWards.get(0).getParent().getId());
        for (Boundary revenue : revWards)
            electionWards.addAll(crossHierarchyService
                    .findChildBoundariesByParentBoundaryIdParentBoundaryTypeAndChildBoundaryType(WARD,
                            REVENUE_HIERARCHY_TYPE, WARD, revenue.getId()));
        model.addAttribute("employeeMappedZone", employeeMappedZone);
        model.addAttribute("mappedRevenueBoundries", revWards);
        model.addAttribute("mappedElectionBoundries", electionWards);
        model.addAttribute("serviceTypeEnumList", getApplicationTypes());
        return "search-document-scrutiny";
    }

    @PostMapping(value = "/bpadocumentscrutiny", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String searchDocumentScrutinyPendingRecords(
            @Valid @ModelAttribute final SearchBpaApplicationForm searchBpaApplicationForm) {
        return new DataTable<>(searchBpaApplicationService.searchForDocumentScrutinyPending(searchBpaApplicationForm),
                searchBpaApplicationForm.draw())
                        .toJson(SearchBpaApplicationAdaptor.class);
    }

    @GetMapping("/details-view/by-permit-number/{permitNumber}")
    public String viewApplicationByPermitNumber(final Model model, @PathVariable final String permitNumber) {
        BpaApplication application = applicationBpaService.findByPermitNumber(permitNumber);
        model.addAttribute("bpaApplication", application);
        model.addAttribute("citizenOrBusinessUser", bpaUtils.logedInuseCitizenOrBusinessUser());
        model.addAttribute(APPLICATION_HISTORY,
                workflowHistoryService.getHistory(application.getAppointmentSchedule(), application.getCurrentState(),
                        application.getStateHistory()));
        model.addAttribute("inspectionList", inspectionService.findByBpaApplicationOrderByIdAsc(application));
        model.addAttribute("lettertopartylist", lettertoPartyService.findByBpaApplicationOrderByIdDesc(application));
        buildReceiptDetails(application.getDemand().getEgDemandDetails(), application.getReceipts());
        return "viewapplication-form";
    }

    @GetMapping("/search/initiate-revocation")
    public String showRevocationSearchForm(final Model model) {
        model.addAttribute(SEARCH_BPA_APPLICATION_FORM, new SearchBpaApplicationForm());
        return "search-revocation-applications";
    }

    @PostMapping(value = "/search/initiate-revocation", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String searchApplicationsForRevocation(
            @Valid @ModelAttribute final SearchBpaApplicationForm searchBpaApplicationForm) {
        List<Long> userIds = new ArrayList<>();
        userIds.add(securityUtils.getCurrentUser().getId());
        return new DataTable<>(searchBpaApplicationService.searchForRevocation(searchBpaApplicationForm, userIds),
                searchBpaApplicationForm.draw())
                        .toJson(SearchBpaApplicationAdaptor.class);
    }

    @GetMapping("/search/cancel-permit")
    public String showPermitCancelSearchForm(final Model model) {
        model.addAttribute("applicationTypes", getApplicationTypes());
        model.addAttribute(SEARCH_BPA_APPLICATION_FORM, new SearchBpaApplicationForm());
        return "search-permitcancel-initiated";
    }

    @PostMapping(value = "/search/cancel-permit", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String searchApplicationsInitiatedForPermitCancel(
            @Valid @ModelAttribute final SearchBpaApplicationForm searchBpaApplicationForm) {
        List<Long> userIds = new ArrayList<>();
        userIds.add(securityUtils.getCurrentUser().getId());
        return new DataTable<>(searchBpaApplicationService.searchForInitForPermitCancel(searchBpaApplicationForm, userIds),
                searchBpaApplicationForm.draw())
                        .toJson(SearchBpaApplicationAdaptor.class);
    }

}
