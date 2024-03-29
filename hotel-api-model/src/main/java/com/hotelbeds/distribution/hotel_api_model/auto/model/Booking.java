/**
 * Autogenerated code by SdkModelGenerator.
 * Do not edit. Any modification on this file will be removed automatically after project build
 *
 */
package com.hotelbeds.distribution.hotel_api_model.auto.model;

/*
 * #%L
 * Hotel API SDK Model
 * %%
 * Copyright (C) 2015 HOTELBEDS TECHNOLOGY, S.L.U.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 2.1 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-2.1.html>.
 * #L%
 */


import java.math.BigDecimal;
import java.time.LocalDate;

import com.hotelbeds.distribution.hotel_api_model.auto.common.SimpleTypes.BookingStatus;
import com.hotelbeds.distribution.hotel_api_model.auto.convert.json.DateSerializer;
import com.hotelbeds.distribution.hotel_api_model.auto.convert.json.RateSerializer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@JsonInclude(Include.NON_NULL)
@ToString
@NoArgsConstructor
@Data
public class Booking {

	private String reference;
	private String cancellationReference;
	private String clientReference;
	@JsonProperty
	@JsonSerialize(using = DateSerializer.class)
	private LocalDate creationDate;
	private BookingStatus status;
	@JsonSerialize(using = RateSerializer.class)
	private BigDecimal agCommision;
	@JsonSerialize(using = RateSerializer.class)
	private BigDecimal commisionVAT;
	private String creationUser;
	private Holder holder;
	private Hotel hotel;
	private String remark;


}