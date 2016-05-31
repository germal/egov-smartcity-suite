<%--
  ~ eGov suite of products aim to improve the internal efficiency,transparency,
  ~    accountability and the service delivery of the government  organizations.
  ~
  ~     Copyright (C) <2015>  eGovernments Foundation
  ~
  ~     The updated version of eGov suite of products as by eGovernments Foundation
  ~     is available at http://www.egovernments.org
  ~
  ~     This program is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU General Public License as published by
  ~     the Free Software Foundation, either version 3 of the License, or
  ~     any later version.
  ~
  ~     This program is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU General Public License for more details.
  ~
  ~     You should have received a copy of the GNU General Public License
  ~     along with this program. If not, see http://www.gnu.org/licenses/ or
  ~     http://www.gnu.org/licenses/gpl.html .
  ~
  ~     In addition to the terms of the GPL license to be adhered to in using this
  ~     program, the following additional terms are to be complied with:
  ~
  ~         1) All versions of this program, verbatim or modified must carry this
  ~            Legal Notice.
  ~
  ~         2) Any misrepresentation of the origin of the material is prohibited. It
  ~            is required that all modified versions of this material be marked in
  ~            reasonable ways as different from the original version.
  ~
  ~         3) This license does not grant any rights to any user of the program
  ~            with regards to rights under trademark law for use of the trade names
  ~            or trademarks of eGovernments Foundation.
  ~
  ~   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
  --%>

<style type="text/css">
.yui-dt table {
	width: 100%;
}

.yui-dt-col-Add {
	width: 5%;
}

.yui-dt-col-Delete {
	width: 5%;
}
</style>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div class="panel panel-primary" data-collapsed="0"
	style="text-align: left">
	<div class="panel-heading">
		<div class="panel-title">
			<spring:message code="lbl.financialdetails" />
		</div>
	</div>
	<div class="panel-body">
		<div class="row add-border">
			<div class="col-xs-3 add-margin">
				<spring:message code="lbl.fund" />
			</div>
			<div class="col-xs-3 add-margin view-content">
				<c:out default="N/A" value="${lineEstimate.fund.name}"></c:out>
			</div>
			<div class="col-xs-3 add-margin">
				<spring:message code="lbl.function" />
			</div>
			<div class="col-xs-3 add-margin view-content">
				<c:out default="N/A" value="${lineEstimate.function.name}"></c:out>
			</div>
		</div>
		<div class="row add-border">
			<div class="col-xs-3 add-margin">
				<spring:message code="lbl.budgethead" />
			</div>
			<div class="col-xs-3 add-margin view-content">
				<c:out default="N/A" value="${lineEstimate.budgetHead.name}" />
			</div>
		</div>
		<div class="row add-border">
			<div class="col-xs-3 add-margin">
				<spring:message code="lbl.scheme" />
			</div>
			<div class="col-xs-3 add-margin view-content">
				<c:out default="N/A" value="${lineEstimate.scheme.name}" />
			</div>
			<div class="col-xs-3 add-margin">
				<spring:message code="lbl.subscheme" />
			</div>
			<div class="col-xs-3 add-margin view-content">
				<c:out default="N/A" value="${lineEstimate.subScheme.name}"></c:out>
			</div>
		</div>
	</div>
</div>

<div class="panel panel-primary" data-collapsed="0">
	<div class="panel-heading">
		<div class="panel-title">
			<spring:message code="header.assetDetails" />
		</div>
	</div>
	<div class="panel-body">
		<table class="table table-bordered" id="tblassetdetails">
			<thead>
				<tr>
					<th><spring:message code="lbl.slno" /></th>
					<th><spring:message code="lbl.nameofasset" /></th>
					<th><spring:message code="lbl.assetcode" /></th>
					<th><spring:message code="lbl.search" /></th>
					<th><spring:message code="lbl.adddel" /></th>
				</tr>
			</thead>
			<tbody id="multiYeaeEstimateTbl">
				<tr id="yearEstimateRow">
					<c:choose>
						<c:when test="${abstractEstimate.assetValues.size() == 0}">
							<td><span class="spansno">1</span></td>
							<td><form:input path="assetValues[0].asset.name" name="assetValues[0].asset.name" /></td>
							<td><form:input path="assetValues[0].asset.name" name="assetValues[0].asset.code" /></td>
						</c:when>
					</c:choose>
			</tbody>
		</table>
		<div class="col-sm-12 text-center">
			<button id="addRowBtn" type="button" class="btn btn-primary"
				onclick="addAssetDetails()">
				<spring:message code="lbl.addrow" />
			</button>
		</div>
	</div>
</div>
