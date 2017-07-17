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

<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/includes/taglibs.jsp"%>
			<table width="100%">
				<tr>
					<td width="20%" align="right" class="greybox">
					<label><s:text name="lift"></s:text> :&nbsp;&nbsp;&nbsp;&nbsp;<s:if
			test="%{propertyDetail.lift == true}">
			<span class="bold"> Yes</span>
		</s:if> <span class="bold"> <s:else> N/A</s:else></span></label> <br />
					<label><s:text name="electricity"></s:text> :&nbsp;&nbsp;&nbsp;&nbsp;<s:if
			test="%{propertyDetail.electricity == true}">
			<span class="bold"> Yes</span>
		</s:if> <span class="bold"> <s:else> N/A</s:else></span></label>
				  </td>
				  <td width="20%" align="right"><label><s:text name="toilets"></s:text> :&nbsp;&nbsp;&nbsp;&nbsp;<s:if
			test="%{propertyDetail.toilets == true}">
			<span class="bold"> Yes</span>
		</s:if> <span class="bold"> <s:else> N/A</s:else></span></label> <br />
						<label><s:text name="attachbathroom"></s:text> :&nbsp;&nbsp;&nbsp;&nbsp;<s:if
			test="%{propertyDetail.attachedBathRoom == true}">
			<span class="bold"> Yes</span>
		</s:if> <span class="bold"> <s:else> N/A</s:else></span></label>
				   </td>
				   <td width="20%" align="right"><label><s:text name="watertap"></s:text> :&nbsp;&nbsp;&nbsp;&nbsp;<s:if
			test="%{propertyDetail.waterTap == true}">
			<span class="bold"> Yes</span>
		</s:if> <span class="bold"> <s:else> N/A</s:else></span></label> <br />
					     <label><s:text
								name="waterharvesting"></s:text> :&nbsp;&nbsp;&nbsp;&nbsp;<s:if
			test="%{propertyDetail.waterHarvesting == true}">
			<span class="bold"> Yes</span>
		</s:if> <span class="bold"> <s:else> N/A</s:else></span></label>
				   </td>
				   <td width="20%" align="right">
				            <label><s:text name="cableconnection"></s:text> :&nbsp;&nbsp;&nbsp;&nbsp;<s:if
			test="%{propertyDetail.cable == true}">
			<span class="bold"> Yes</span>
		</s:if> <span class="bold"> <s:else> N/A</s:else></span></label><br/>
				            </td>
					<td width="10%"></td>
				</tr>
			</table>
