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

<div id="overheadsHeaderTable" class="panel panel-primary" data-collapsed="0">

<div class="panel-heading custom_form_panel_heading" >
	<div class="panel-title">
	   <spring:message code="lbl.overheads" />
	   <div class="pull-right">
	    <a href="javascript:void(0);" class="btn btn-primary" 
	  	       onclick="recalculateOverheads();">
   	        <spring:message code="lbl.recalculate" />
   	    </a>
	   </div>
	</div>
</div>
<div class="panel-body">
		<table class="table table-bordered" id="overheadTable">
			<thead>
				<tr>
					<th><spring:message code="lbl.name"/></th>
					<th><spring:message code="lbl.percentage"/></th>
					<th><spring:message code="lbl.amount"/></th>
					<th><spring:message code="lbl.action"/></th> 					
				</tr>
			</thead>
			<tbody>
					<tr id="overheadRow">
							<td>
								<form:select path="" data-first-option="false" name="overheadValues[0].name" id="overheadValues[0].name" class="form-control" onchange="getPercentageOrLumpsumByOverhead(this);">
									<form:option value="">
										<spring:message code="lbl.select" />
									</form:option>
									<c:forEach var="overhead" items="${overheads}">
										<form:option value="${overhead.id}">
											<c:out value="${overhead.name}" />
										</form:option>
									</c:forEach>
								</form:select> 
								<form:hidden path="overheadValues[0].overhead.id"  name="overheadValues[0].overhead.id" id="overheadValues[0].overhead.id"  class="form-control table-input hidden-input"/> 
								<form:errors path="overheadValues[0].overhead.id" cssClass="add-margin error-msg" /> 
							</td>
							<td>
								<input type="text" id="overheadValues[0].percentage" name="overheadValues[0].percentage"  class="form-control" disabled>  
							</td>
							<td>
								<form:input path="overheadValues[0].amount" id="overheadValues[0].amount" name="overheadValues[0].amount"  data-pattern="decimalvalue" data-idx="0" data-optional="0" class="form-control table-input text-right" onblur="calculateOverheadTotalAmount();"  maxlength="12" />
								<form:errors path="overheadValues[0].amount" cssClass="add-margin error-msg" /> 
							</td> 
							<td class="text-center"><span style="cursor:pointer;" onclick="addOverheadRow();"><i class="fa fa-plus"></i></span>
							 <span class="add-padding" onclick="deleteOverheadRow(this);"><i class="fa fa-trash" data-toggle="tooltip" title="" data-original-title="Delete!"></i></span> </td>
						</tr>
			</tbody>
		</table>
		<table class="table table-bordered" >
			<tr>
				<td width="64.5%" style="text-align:right"><spring:message code="lbl.total" /></td>
				<td>
					<input type="text" id="overheadTotalAmount" name="overheadTotalAmount"  class="form-control" disabled>
				</td>
				<td width="7.5%"></td>
			</tr>
		</table>
</div>

</div>