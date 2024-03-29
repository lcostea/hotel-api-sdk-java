/**
 * Autogenerated code by SdkModelGenerator.
 * Do not edit. Any modification on this file will be removed automatically after project build
 *
 */
package com.hotelbeds.distribution.hotel_api_model.auto.messages;

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


import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.hotelbeds.distribution.hotel_api_model.auto.annotation.validators.ValidLimitFilter;
import com.hotelbeds.distribution.hotel_api_model.auto.annotation.validators.ValidOccupancies;
import com.hotelbeds.distribution.hotel_api_model.auto.annotation.validators.ValidReviewFilter;
import com.hotelbeds.distribution.hotel_api_model.auto.annotation.validators.ValidStay;
import com.hotelbeds.distribution.hotel_api_model.auto.common.SimpleTypes.Accommodation;
import com.hotelbeds.distribution.hotel_api_model.auto.model.Boards;
import com.hotelbeds.distribution.hotel_api_model.auto.model.Destination;
import com.hotelbeds.distribution.hotel_api_model.auto.model.Filter;
import com.hotelbeds.distribution.hotel_api_model.auto.model.GeoLocation;
import com.hotelbeds.distribution.hotel_api_model.auto.model.HotelsFilter;
import com.hotelbeds.distribution.hotel_api_model.auto.model.KeywordsFilter;
import com.hotelbeds.distribution.hotel_api_model.auto.model.Occupancy;
import com.hotelbeds.distribution.hotel_api_model.auto.model.ReviewFilter;
import com.hotelbeds.distribution.hotel_api_model.auto.model.Rooms;
import com.hotelbeds.distribution.hotel_api_model.auto.model.Source;
import com.hotelbeds.distribution.hotel_api_model.auto.model.Stay;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@JsonInclude(Include.NON_NULL)
@ToString
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper=false)
public class AvailabilityRQ extends AbstractGenericRequest {

	@NotNull
	@Valid
	@ValidStay(maxDaysRange = 30)
	private Stay stay;
	@Valid
	@ValidOccupancies(maxRooms = 5)
	private List<Occupancy> occupancies;
	@Valid
	private GeoLocation geolocation;
	@Valid
	private Destination destination;
	@JsonProperty("keywords")
	@Valid
	private KeywordsFilter keywordsFilter;
	@JsonProperty("hotels")
	@Valid
	private HotelsFilter hotelsFilter;
	@JsonProperty("reviews")
	@Valid
	@ValidReviewFilter
	private List<ReviewFilter> reviewsFilter;
	@Valid
	@ValidLimitFilter
	private Filter filter;
	@Valid
	private Boards boards;
	@Valid
	private Rooms rooms;
	private Boolean dailyRate;
	private String sourceMarket;
	@Valid
	private List<Accommodation> accommodations;
	@Valid
	private Source source;


}