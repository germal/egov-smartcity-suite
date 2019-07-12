/*
 *    eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) 2017  eGovernments Foundation
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
 *            Further, all user interfaces, including but not limited to citizen facing interfaces,
 *            Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *            derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *            For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *            For any further queries on attribution, including queries on brand guidelines,
 *            please contact contact@egovernments.org
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
 *
 */
jQuery(document)
    .ready(
        function () {

            if ($('#sentToPreviousOwner').val() === 'true' &&
                $('#approveComments').val() &&
                $('#wfstateDesc').val() !== 'LP Created' &&
                $('#wfstateDesc').val() !== 'LP Reply Received') {
                $('#showCommentsModal').html($('#approveComments').val());
                $('#commentsModal').modal('show');
            }

            var row = '<tr>' +
                '<td class="text-center"><span class="serialNo text-center" id="slNoInsp">{{sno}}</span><input type="hidden" name="additionalRejectReasonsTemp[{{idx}}].oc" value="{{applicationId}}" /><input type="hidden" class="additionalPermitCondition" name="additionalRejectReasonsTemp[{{idx}}].type" value="ADDITIONAL_PERMITCONDITION"/><input type="hidden" class="additionalPermitCondition" name="additionalRejectReasonsTemp[{{idx}}].noticeCondition" value="{{permitConditionId}}"/><input type="hidden" class="serialNo" data-sno name="additionalRejectReasonsTemp[{{idx}}].orderNumber"/></td>' +
                '<td><textarea class="form-control patternvalidation additionalPermitCondition" data-pattern="alphanumericspecialcharacters" rows="2" maxlength="500" name="additionalRejectReasonsTemp[{{idx}}].additionalCondition"/></td>';

            var tbody = $('#bpaAdditionalRejectionReasons').children('tbody');
            var table = tbody.length ? tbody : $('#bpaAdditionalRejectionReasons');
            $('#addAddnlRejectRow').click(function () {
                var idx = $(tbody).find('tr').length;
                //Add row
                var row = {
                    'sno': idx + 1,
                    'idx': idx,
                    'permitConditionId': $('#additionalPermitCondition').val(),
                    'applicationId': $('#scrutinyapplicationid').val()
                };
                addRowFromObject(row);
                patternvalidation();
            });

            function addRowFromObject(rowJsonObj) {
                table.append(row.compose(rowJsonObj));
            }

            String.prototype.compose = (function () {
                var re = /\{{(.+?)\}}/g;
                return function (o) {
                    return this.replace(re, function (_, k) {
                        return typeof o[k] != 'undefined' ? o[k] : '';
                    });
                }
            }());

            if ($('#townSurveyorInspectionRequire:checked').length == 0) {
                $('#townSurveyorInspectionRequire').val(false);
            }
            $("#townSurveyorInspectionRequire").click(function () {
                if ($('#townSurveyorInspectionRequire').is(':checked')) {
                    $('#townSurveyorInspectionRequire').attr('checked', 'true');
                    $('#townSurveyorInspectionRequire').val(true);
                } else {
                    $('#townSurveyorInspectionRequire').attr('checked', 'false');
                    $('#townSurveyorInspectionRequire').val(false);
                }
            });

            $(".rejectionReasons").change(function () {
                setCheckBoxValue($(this));
            });

            function setCheckBoxValue(currentVal) {
                var $hiddenName = currentVal.data('change-to');
                if (currentVal.is(':checked')) {
                    $('input[name="' + $hiddenName + '"]').val(true);
                } else {
                    $('input[name="' + $hiddenName + '"]').val(false);
                }
            }

            // toggle between multiple tab
            jQuery('form')
                .validate(
                    {
                        ignore: ".ignore",
                        invalidHandler: function (e, validator) {
                            if (validator.errorList.length)
                                $(
                                    '#settingstab a[href="#'
                                    + jQuery(
                                    validator.errorList[0].element)
                                        .closest(
                                            ".tab-pane")
                                        .attr(
                                            'id')
                                    + '"]').tab(
                                    'show');
                        }
                    });

            var validator = $("#occupancyCertificateUpdateForm").validate({
                highlight: function (element, errorClass) {
                    $(element).fadeOut(function () {
                        $(element).fadeIn();
                    });
                }
            });

            $(".workAction")
                .click(
                    function (e) {
                        var action = document
                            .getElementById("workFlowAction").value;
                        if (action === 'Reject') {
                            $('#Reject').attr('formnovalidate', 'true');
                            if (validateForm(validator) && validateOnReject(true)) {
                                bootbox
                                    .confirm({
                                        message: 'Please confirm, Do you really want to Reject the application ?',
                                        buttons: {
                                            'cancel': {
                                                label: 'No',
                                                className: 'btn-danger'
                                            },
                                            'confirm': {
                                                label: 'Yes',
                                                className: 'btn-primary'
                                            }
                                        },
                                        callback: function (result) {
                                            if (result) {
                                                $('#occupancyCertificateUpdateForm').trigger('submit');
                                            } else {
                                                e.stopPropagation();
                                                e.preventDefault();
                                            }
                                        }
                                    });
                            } else {
                                e.preventDefault();
                            }
                            return false;
                        } else if (action === 'Initiate Rejection') {
                            if (validateForm(validator) && validateOnReject(true)) {
                                bootbox
                                    .confirm({
                                        message: 'Please confirm, Do you really want to initiate rejection for this the application ?',
                                        buttons: {
                                            'cancel': {
                                                label: 'No',
                                                className: 'btn-danger'
                                            },
                                            'confirm': {
                                                label: 'Yes',
                                                className: 'btn-primary'
                                            }
                                        },
                                        callback: function (result) {
                                            if (result) {
                                                $('#occupancyCertificateUpdateForm').trigger('submit');
                                            } else {
                                                e.stopPropagation();
                                                e.preventDefault();
                                            }
                                        }
                                    });
                            } else {
                                e.preventDefault();
                            }
                            return false;
                        } else if (action === 'Revert') {
                            if (validateForm(validator) && validateOnRevert()) {
                                bootbox
                                    .confirm({
                                        message: 'Please confirm, Do you really want to send back this application to previous approved official ?',
                                        buttons: {
                                            'cancel': {
                                                label: 'No',
                                                className: 'btn-danger'
                                            },
                                            'confirm': {
                                                label: 'Yes',
                                                className: 'btn-primary'
                                            }
                                        },
                                        callback: function (result) {
                                            if (result) {
                                                $('#occupancyCertificateUpdateForm').trigger('submit');
                                            } else {
                                                e.stopPropagation();
                                                e.preventDefault();
                                            }
                                        }
                                    });
                            } else {
                                e.preventDefault();
                            }
                            return false;
                        } else if (action === 'Approve') {
                            if (validateOnApproveAndForward(validator, action)) {
                                bootbox
                                    .confirm({
                                        message: 'Please confirm, Do you really want to approve this application ?',
                                        buttons: {
                                            'cancel': {
                                                label: 'No',
                                                className: 'btn-danger'
                                            },
                                            'confirm': {
                                                label: 'Yes',
                                                className: 'btn-primary'
                                            }
                                        },
                                        callback: function (result) {
                                            if (result) {
                                                $('#occupancyCertificateUpdateForm').trigger('submit');
                                            } else {
                                                e.stopPropagation();
                                                e.preventDefault();
                                            }
                                        }
                                    });
                            } else {
                                e.preventDefault();
                            }
                            return false;
                        } else if (action === 'Forward') {
                            if (validateOnApproveAndForward(validator, action) && validateAdditionalConditionsOnFwd()) {
                                bootbox
                                    .confirm({
                                        message: 'Please confirm, Do you really want to forward this application ?',
                                        buttons: {
                                            'cancel': {
                                                label: 'No',
                                                className: 'btn-danger'
                                            },
                                            'confirm': {
                                                label: 'Yes',
                                                className: 'btn-primary'
                                            }
                                        },
                                        callback: function (result) {
                                            if (result) {
                                                $('#occupancyCertificateUpdateForm').trigger('submit');
                                            } else {
                                                e.stopPropagation();
                                                e.preventDefault();
                                            }
                                        }
                                    });
                            } else {
                                e.preventDefault();
                            }
                            return false;
                        }else if (action == 'Forward to Overseer') {
                            if (validateOnNocVerifyForward()) {
                                bootbox
                                    .confirm({
                                        message: 'Please confirm, Do you really want to forward this application to overseer ?',
                                        buttons: {
                                            'cancel': {
                                                label: 'No',
                                                className: 'btn-danger'
                                            },
                                            'confirm': {
                                                label: 'Yes',
                                                className: 'btn-primary'
                                            }
                                        },
                                        callback: function (result) {
                                            if (result) {
                                                $('#occupancyCertificateUpdateForm').trigger('submit');
                                            } else {
                                                e.stopPropagation();
                                                e.preventDefault();
                                            }
                                        }
                                    });
                            } else {
                                e.preventDefault();
                            }
                            return false;
                        } else if (action == 'Forward to Superintendent') {
                            if (validateOnNocVerifyForward()) {
                                bootbox
                                    .confirm({
                                        message: 'Please confirm, Do you really want to forward this application to superintendent ?',
                                        buttons: {
                                            'cancel': {
                                                label: 'No',
                                                className: 'btn-danger'
                                            },
                                            'confirm': {
                                                label: 'Yes',
                                                className: 'btn-primary'
                                            }
                                        },
                                        callback: function (result) {
                                            if (result) {
                                                $('#occupancyCertificateUpdateForm').trigger('submit');
                                            } else {
                                                e.stopPropagation();
                                                e.preventDefault();
                                            }
                                        }
                                    });
                            } else {
                                e.preventDefault();
                            }
                            return false;
                        } else if (action == 'Forward to Initiator') {
                            if (validateForm(validator,action)) {
                                bootbox
                                    .confirm({
                                        message: 'Please confirm, Do you really want to forward this application to initiator ?',
                                        buttons: {
                                            'cancel': {
                                                label: 'No',
                                                className: 'btn-danger'
                                            },
                                            'confirm': {
                                                label: 'Yes',
                                                className: 'btn-primary'
                                            }
                                        },
                                        callback: function (result) {
                                            if (result) {
                                                $('#occupancyCertificateUpdateForm').trigger('submit');
                                            } else {
                                                e.stopPropagation();
                                                e.preventDefault();
                                            }
                                        }
                                    });
                            } else {
                                e.preventDefault();
                            }
                            return false;
                        } else if (action == 'Forward to Secretary') {
                            if (validateOnApproveAndForward(validator, action)) {
                                bootbox
                                    .confirm({
                                        message: 'Please confirm, Do you really want to forward this application to Secretary ?',
                                        buttons: {
                                            'cancel': {
                                                label: 'No',
                                                className: 'btn-danger'
                                            },
                                            'confirm': {
                                                label: 'Yes',
                                                className: 'btn-primary'
                                            }
                                        },
                                        callback: function (result) {
                                            if (result) {
                                                $('#occupancyCertificateUpdateForm').trigger('submit');
                                            } else {
                                                e.stopPropagation();
                                                e.preventDefault();
                                            }
                                        }
                                    });
                            } else {
                                e.preventDefault();
                            }
                            return false;
                        } else if (action === 'Generate Occupancy Certificate') {
                            $('#Generate Occupancy Certificate').attr('formnovalidate', 'true');
                            if (validateOnApproveAndForward(validator, action)) {
                                bootbox
                                    .confirm({
                                        message: 'Please confirm, Do you really want to Generate Occupancy Certificate.',
                                        buttons: {
                                            'cancel': {
                                                label: 'No',
                                                className: 'btn-danger'
                                            },
                                            'confirm': {
                                                label: 'Yes',
                                                className: 'btn-primary'
                                            }
                                        },
                                        callback: function (result) {
                                            if (result) {
                                                $('#occupancyCertificateUpdateForm').trigger('submit');
                                            } else {
                                                e.stopPropagation();
                                                e.preventDefault();
                                            }
                                        }
                                    });
                            } else {
                                e.preventDefault();
                            }
                            return false;
                        } else if (action === 'Generate Rejection Notice') {
                            if (validateOnApproveAndForward(validator, action) && validateOnReject(false)) {
                                bootbox
                                    .confirm({
                                        message: 'Please confirm, All required rejection conditions are added and going to generate rejection notice with selected rejection conditions.',
                                        buttons: {
                                            'cancel': {
                                                label: 'No',
                                                className: 'btn-danger'
                                            },
                                            'confirm': {
                                                label: 'Yes',
                                                className: 'btn-primary'
                                            }
                                        },
                                        callback: function (result) {
                                            if (result) {
                                                $('#occupancyCertificateUpdateForm').trigger('submit');
                                            } else {
                                                e.stopPropagation();
                                                e.preventDefault();
                                            }
                                        }
                                    });
                            } else {
                                e.preventDefault();
                            }
                            return false;
                        } else {
                            validateOnApproveAndForward(validator, action);
                        }
                    });

            $("#btnSave")
                .click(
                    function (e) {
                        document
                            .getElementById("workFlowAction").value = 'Save';
                        if(validateForm(validator, 'Save'))
                            $('#occupancyCertificateUpdateForm').trigger('submit');
                    });

        });

function validateAdditionalConditionsOnFwd() {
    var approvalComent = $('#approvalComent').val();
    if (($("#approvalDesignation option:selected").text() === 'Superintendent' && $('#townSurveyorInspectionRequire').val() === 'true')) {
        bootbox.alert("Please make sure, Request for town surveyor field inspection is recommended but you are trying to forward the application to Superintendent, please select Town Surveyor as approver designation from below otherwise uncheck Request for town surveyor field inspection if town surveyor field inspection not require,");
        return false;
    } else if (($("#approvalDesignation option:selected").text() === 'Town Surveyor' && $('#townSurveyorInspectionRequire').val() === 'false')) {
        bootbox.alert("Please select checkbox Request for town surveyor field inspection if you want to forward the application to Town Surveyor");
        return false;
    } else if (($("#approvalDesignation option:selected").text() === 'Town Surveyor' || $('#townSurveyorInspectionRequire').val() === 'true') && approvalComent === "") {
        $('#approvalComent').focus();
        bootbox.alert("Please enter comments/reason for town surveyor inspection");
        return false;
    }
    return true;
}

function validateOnReject(isCommentsRequire) {
    var approvalComent = $('#approvalComent').val();
    var rejectionReasonsLength = $('.rejectionReasons:checked').length;
    if (rejectionReasonsLength <= 0) {
        $('.rejectionReason').show();
        bootbox.alert('Please select at least one rejection reason is mandatory');
        return false;
    } else if (approvalComent === "" && isCommentsRequire) {
        bootbox.alert("Please enter rejection comments/reason");
        $('#approvalComent').focus();
        return false;
    }
    return true;
}

function validateOnRevert() {
    var approvalComent = $('#approvalComent').val();
    if (approvalComent === "") {
        $('#approvalComent').focus();
        bootbox.alert("Please enter comments/reason for sending back to previous official");
        return false;
    }
    return true;
}

function validateForm(validator, action) {
	if('Generate Occupancy Certificate' !== action 
			&& 'Save' !== action 
			&& 'Generate Rejection Notice' !== action 
			&& 'Forward to Initiator' !== action
			&& !$('#approvalComent').val()) {
		bootbox.alert("Please enter comments, comments are mandatory.");
	} else if ($('#occupancyCertificateUpdateForm').valid()) {
        return true;
    } else {
        $errorInput = undefined;

        $.each(validator.invalidElements(), function (index, elem) {

            if (!$(elem).is(":visible") && !$(elem).val() && index === 0
                && $(elem).closest('div').find('.bootstrap-tagsinput').length > 0) {
                $errorInput = $(elem);
            }

            if (!$(elem).is(":visible") && !$(elem).closest('div.panel-body').is(":visible")) {
                $(elem).closest('div.panel-body').show();
                console.log("elem", $(elem));
            }
        });

        if ($errorInput)
            $errorInput.tagsinput('focus');

        validator.focusInvalid();
        return false;
    }
}


function validateOnApproveAndForward(validator, action) {
    validateWorkFlowApprover(action);
    if ($('#wfstateDesc').val() === 'NEW') {
        $('#approvalDepartment').removeAttr('required');
        $('#approvalDesignation').removeAttr('required');
        $('#approvalPosition').removeAttr('required');
        return true;
    } else {
        return validateForm(validator,action);
    }
}

function validateOnNocVerifyForward() {
    var approvalComent = $('#approvalComent').val();
    if (approvalComent == "") {
        $('#approvalComent').focus();
        bootbox.alert("Please enter comments/reason for sending to overseer or superintendent");
        return false;
    }
    return true;
}