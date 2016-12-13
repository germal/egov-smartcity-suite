/*
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 *         1) All versions of this program, verbatim or modified must carry this
 *            Legal Notice.
 *
 *         2) Any misrepresentation of the origin of the material is prohibited. It
 *            is required that all modified versions of this material be marked in
 *            reasonable ways as different from the original version.
 *
 *         3) This license does not grant any rights to any user of the program
 *            with regards to rights under trademark law for use of the trade names
 *            or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.works.web.controller.contractoradvance;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.egov.commons.service.ChartOfAccountsService;
import org.egov.eis.web.contract.WorkflowContainer;
import org.egov.eis.web.controller.workflow.GenericWorkFlowController;
import org.egov.works.contractoradvance.entity.ContractorAdvanceRequisition;
import org.egov.works.contractoradvance.service.ContractorAdvanceService;
import org.egov.works.mb.service.MBHeaderService;
import org.egov.works.utils.WorksConstants;
import org.egov.works.utils.WorksUtils;
import org.egov.works.workorder.entity.WorkOrderEstimate;
import org.egov.works.workorder.service.WorkOrderEstimateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.JsonObject;

@Controller
@RequestMapping(value = "/contractoradvance")
public class CreateContractorAdvanceController extends GenericWorkFlowController {

    @Autowired
    private WorkOrderEstimateService workOrderEstimateService;

    @Autowired
    private ChartOfAccountsService chartOfAccountsService;

    @Autowired
    private ContractorAdvanceService contractorAdvanceService;

    @Autowired
    private WorksUtils worksUtils;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private MBHeaderService mbHeaderService;

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String showNewForm(
            @ModelAttribute("contractorAdvanceRequisition") final ContractorAdvanceRequisition contractorAdvanceRequisition,
            final Model model, final HttpServletRequest request) {
        final String woeId = request.getParameter("woeId");
        final WorkOrderEstimate workOrderEstimate = workOrderEstimateService
                .getWorkOrderEstimateById(Long.valueOf(woeId));
        prepareWorkflow(model, contractorAdvanceRequisition, new WorkflowContainer());
        final Double advancePaidTillNow = contractorAdvanceService.getTotalAdvancePaid(
                contractorAdvanceRequisition.getId() == null ? -1L : contractorAdvanceRequisition.getId(),
                Long.valueOf(woeId),
                ContractorAdvanceRequisition.ContractorAdvanceRequisitionStatus.APPROVED.toString());
        final Double totalMBAmountOfMBs = mbHeaderService.getTotalMBAmountOfMBs(null,
                Long.valueOf(woeId),
                ContractorAdvanceRequisition.ContractorAdvanceRequisitionStatus.CANCELLED.toString());
        model.addAttribute("advancePaidTillNow", advancePaidTillNow);
        model.addAttribute("totalMBAmountOfMBs", totalMBAmountOfMBs);
        model.addAttribute("documentDetails", contractorAdvanceRequisition.getDocumentDetails());
        model.addAttribute("stateType", contractorAdvanceRequisition.getClass().getSimpleName());
        model.addAttribute("woeId", woeId);
        model.addAttribute("mode", "new");
        model.addAttribute("workOrderEstimate", workOrderEstimate);
        model.addAttribute("contractorAdvanceRequisition", contractorAdvanceRequisition);
        setDropDownValues(model);
        return "contractorAdvance-form";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(
            @ModelAttribute("contractorAdvanceRequisition") final ContractorAdvanceRequisition contractorAdvanceRequisition,
            final Model model, final BindingResult resultBinder, final HttpServletRequest request,
            @RequestParam String workFlowAction, @RequestParam("file") final MultipartFile[] files) throws IOException {

        final JsonObject jsonObject = new JsonObject();
        contractorAdvanceService.validateARFInDrafts(contractorAdvanceRequisition.getId(),
                contractorAdvanceRequisition.getWorkOrderEstimate().getId(), jsonObject,
                resultBinder);
        contractorAdvanceService.validateARFInWorkFlow(contractorAdvanceRequisition.getId(),
                contractorAdvanceRequisition.getWorkOrderEstimate().getId(), jsonObject,
                resultBinder);
        contractorAdvanceService.validateInput(contractorAdvanceRequisition, resultBinder);

        if (resultBinder.hasErrors()) {
            setDropDownValues(model);
            final Double advancePaidTillNow = contractorAdvanceService.getTotalAdvancePaid(
                    contractorAdvanceRequisition.getId() == null ? -1L : contractorAdvanceRequisition.getId(),
                    contractorAdvanceRequisition.getWorkOrderEstimate().getId(),
                    ContractorAdvanceRequisition.ContractorAdvanceRequisitionStatus.APPROVED.toString());
            final Double totalMBAmountOfMBs = mbHeaderService.getTotalMBAmountOfMBs(null,
                    contractorAdvanceRequisition.getWorkOrderEstimate().getId(),
                    ContractorAdvanceRequisition.ContractorAdvanceRequisitionStatus.CANCELLED.toString());
            prepareWorkflow(model, contractorAdvanceRequisition, new WorkflowContainer());
            model.addAttribute("advancePaidTillNow", advancePaidTillNow);
            model.addAttribute("totalMBAmountOfMBs", totalMBAmountOfMBs);
            model.addAttribute("documentDetails", contractorAdvanceRequisition.getDocumentDetails());
            model.addAttribute("stateType", contractorAdvanceRequisition.getClass().getSimpleName());
            model.addAttribute("woeId", contractorAdvanceRequisition.getWorkOrderEstimate().getId());
            model.addAttribute("mode", "new");
            model.addAttribute("workOrderEstimate", contractorAdvanceRequisition.getWorkOrderEstimate());
            model.addAttribute("contractorAdvanceRequisition", contractorAdvanceRequisition);

            return "contractorAdvance-form";
        } else {

            Long approvalPosition = 0l;
            String approvalComment = "";
            if (request.getParameter("approvalComment") != null)
                approvalComment = request.getParameter("approvalComent");
            if (request.getParameter("workFlowAction") != null)
                workFlowAction = request.getParameter("workFlowAction");
            if (request.getParameter("approvalPosition") != null && !request.getParameter("approvalPosition").isEmpty())
                approvalPosition = Long.valueOf(request.getParameter("approvalPosition"));

            final ContractorAdvanceRequisition savedContractorAdvanceRequisition = contractorAdvanceService.create(
                    contractorAdvanceRequisition, files,
                    approvalPosition, approvalComment, null, workFlowAction);
            final String pathVars = worksUtils.getPathVars(savedContractorAdvanceRequisition.getStatus(),
                    savedContractorAdvanceRequisition.getState(), savedContractorAdvanceRequisition.getId(), approvalPosition);

            return "redirect:/contractoradvance/contractoradvance-success?pathVars=" + pathVars + "&arfNumber="
                    + savedContractorAdvanceRequisition.getAdvanceRequisitionNumber();
        }
    }

    @RequestMapping(value = "/contractoradvance-success", method = RequestMethod.GET)
    public String showContractorAdvanceSuccessPage(@RequestParam("arfNumber") final String arfNumber, final Model model,
            final HttpServletRequest request) {

        final String[] keyNameArray = request.getParameter("pathVars").split(",");
        Long id = 0L;
        String approverName = "";
        String currentUserDesgn = "";
        String nextDesign = "";
        if (keyNameArray.length != 0 && keyNameArray.length > 0)
            if (keyNameArray.length == 1)
                id = Long.parseLong(keyNameArray[0]);
            else if (keyNameArray.length == 3) {
                id = Long.parseLong(keyNameArray[0]);
                approverName = keyNameArray[1];
                currentUserDesgn = keyNameArray[2];
            } else {
                id = Long.parseLong(keyNameArray[0]);
                approverName = keyNameArray[1];
                currentUserDesgn = keyNameArray[2];
                nextDesign = keyNameArray[3];
            }

        if (id != null)
            model.addAttribute("approverName", approverName);
        model.addAttribute("currentUserDesgn", currentUserDesgn);
        model.addAttribute("nextDesign", nextDesign);

        final ContractorAdvanceRequisition contractorAdvanceRequisition = contractorAdvanceService
                .getContractorAdvanceByARFNumber(arfNumber);

        final String message = getMessageByStatus(contractorAdvanceRequisition, approverName, nextDesign);
        model.addAttribute("message", message);

        return "contractorAdvance-success";
    }

    private String getMessageByStatus(final ContractorAdvanceRequisition contractorAdvanceRequisition, final String approverName,
            final String nextDesign) {
        String message = "";

        if (contractorAdvanceRequisition.getStatus().getCode()
                .equals(ContractorAdvanceRequisition.ContractorAdvanceRequisitionStatus.CREATED.toString()))
            message = messageSource.getMessage("msg.contractoradvance.create.success",
                    new String[] { contractorAdvanceRequisition.getAdvanceRequisitionNumber(), approverName, nextDesign }, null);
        else if (contractorAdvanceRequisition.getStatus().getCode()
                .equalsIgnoreCase(ContractorAdvanceRequisition.ContractorAdvanceRequisitionStatus.APPROVED.toString()))
            message = messageSource.getMessage("msg.contractoradvance.approved.success",
                    new String[] { contractorAdvanceRequisition.getAdvanceRequisitionNumber() }, null);
        else if (contractorAdvanceRequisition.getStatus().getCode()
                .equalsIgnoreCase(ContractorAdvanceRequisition.ContractorAdvanceRequisitionStatus.RESUBMITTED.toString()))
            message = messageSource.getMessage("msg.contractoradvance.resubmit.success",
                    new String[] { contractorAdvanceRequisition.getAdvanceRequisitionNumber(), approverName, nextDesign }, null);
        else if (contractorAdvanceRequisition.getState().getValue().equalsIgnoreCase(WorksConstants.WF_STATE_REJECTED))
            message = messageSource.getMessage("msg.contractoradvance.reject",
                    new String[] { contractorAdvanceRequisition.getAdvanceRequisitionNumber(), approverName, nextDesign }, null);
        else if (contractorAdvanceRequisition.getState().getValue().equalsIgnoreCase(WorksConstants.WF_STATE_CANCELLED))
            message = messageSource.getMessage("msg.contractoradvance.cancel",
                    new String[] { contractorAdvanceRequisition.getAdvanceRequisitionNumber() }, null);
        else if (contractorAdvanceRequisition.getStatus().getCode().equalsIgnoreCase(WorksConstants.WF_STATE_REJECTED))
            message = messageSource.getMessage("msg.contractoradvance.forward.success",
                    new String[] { contractorAdvanceRequisition.getAdvanceRequisitionNumber(), approverName, nextDesign }, null);
        else if (contractorAdvanceRequisition.getStatus().getCode().equalsIgnoreCase(WorksConstants.CHECKED_STATUS))
            message = messageSource.getMessage("msg.contractoradvance.checked.success",
                    new String[] { contractorAdvanceRequisition.getAdvanceRequisitionNumber(), approverName, nextDesign }, null);

        return message;
    }

    private void setDropDownValues(final Model model) {
        model.addAttribute("debitAccounts",
                chartOfAccountsService.getAccountCodeByPurposeName(WorksConstants.CONTRACTOR_ADVANCE_PURPOSE));
        model.addAttribute("creditAccounts",
                chartOfAccountsService.getAccountCodeByPurposeName(WorksConstants.CONTRACTOR_NETPAYABLE_PURPOSE));
    }
}
