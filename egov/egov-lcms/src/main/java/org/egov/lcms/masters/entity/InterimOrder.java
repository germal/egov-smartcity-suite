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
package org.egov.lcms.masters.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.hibernate.envers.AuditOverride;
import org.hibernate.envers.AuditOverrides;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Table(name = "eglc_interimtype_master")
@Unique(id = "id", tableName = "eglc_interimtype_master", columnName = { "code", "interimOrderType" }, fields = {
        "code", "interimOrderType" }, enableDfltMsg = true)
@SequenceGenerator(name = InterimOrder.SEQ_INTERIMORDER, sequenceName = InterimOrder.SEQ_INTERIMORDER, allocationSize = 1)
@AuditOverrides({ @AuditOverride(forClass = AbstractAuditable.class, name = "lastModifiedBy"),
        @AuditOverride(forClass = AbstractAuditable.class, name = "lastModifiedDate") })
public class InterimOrder extends AbstractAuditable {

    private static final long serialVersionUID = 796823780349590496L;
    public static final String SEQ_INTERIMORDER = "SEQ_EGLC_INTERIMTYPE_MASTER";

    @Id
    @GeneratedValue(generator = SEQ_INTERIMORDER, strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @SafeHtml
    @Length(max = 32)
    @Audited
    private String interimOrderType;

    @NotNull
    @SafeHtml
    @Length(max = 25)
    @Audited
    private String code;

    @SafeHtml
    private String description;

    @Min(1)
    @Max(1000)
    private Long orderNumber;

    @NotNull
    private Boolean active;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public String getInterimOrderType() {
        return interimOrderType;
    }

    public void setInterimOrderType(final String interimOrderType) {
        this.interimOrderType = interimOrderType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Long getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(final Long orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(final Boolean active) {
        this.active = active;
    }

}
