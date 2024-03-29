package com.hotelbeds.distribution.hotel_api_sdk.helpers;

/*
 * #%L
 * hotel-api-sdk
 * %%
 * Copyright (C) 2015 HOTELBEDS, S.L.U.
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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import com.hotelbeds.distribution.hotel_api_model.auto.common.SimpleTypes.Accommodation;
import com.hotelbeds.distribution.hotel_api_model.auto.common.SimpleTypes.HotelCodeType;
import com.hotelbeds.distribution.hotel_api_model.auto.common.SimpleTypes.HotelbedsCustomerType;
import com.hotelbeds.distribution.hotel_api_model.auto.common.SimpleTypes.ReviewsType;
import com.hotelbeds.distribution.hotel_api_model.auto.common.SimpleTypes.ShowDirectPayment;
import com.hotelbeds.distribution.hotel_api_model.auto.messages.AvailabilityRQ;
import com.hotelbeds.distribution.hotel_api_model.auto.model.Boards;
import com.hotelbeds.distribution.hotel_api_model.auto.model.Destination;
import com.hotelbeds.distribution.hotel_api_model.auto.model.Filter;
import com.hotelbeds.distribution.hotel_api_model.auto.model.GeoLocation;
import com.hotelbeds.distribution.hotel_api_model.auto.model.HotelsFilter;
import com.hotelbeds.distribution.hotel_api_model.auto.model.KeywordsFilter;
import com.hotelbeds.distribution.hotel_api_model.auto.model.Occupancy;
import com.hotelbeds.distribution.hotel_api_model.auto.model.Pax;
import com.hotelbeds.distribution.hotel_api_model.auto.model.ReviewFilter;
import com.hotelbeds.distribution.hotel_api_model.auto.model.Rooms;
import com.hotelbeds.distribution.hotel_api_model.auto.model.Stay;
import com.hotelbeds.distribution.hotel_api_model.util.UnitMeasure;
import com.hotelbeds.distribution.hotel_api_sdk.helpers.AvailRoom.AvailRoomBuilder;
import com.hotelbeds.distribution.hotel_api_sdk.helpers.RoomDetail.GuestType;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import lombok.ToString;
import lombok.Value;

@Builder
@Value
@ToString
public class Availability {

    public enum Matcher {
        ALL,
        ANY
    }

    public enum Pay {
        AT_WEB,
        AT_HOTEL,
        INDIFFERENT
    }

    public static interface DelimitedShape {
    }

    @Data
    public static class Circle implements DelimitedShape {
        @NotNull
        private final String longitude;
        @NotNull
        private final String latitude;
        @Min(value = 0)
        private final long radiusInKilometers;
    }

    @Data
    public static class Square implements DelimitedShape {
        @NotNull
        private final String northEastLatitude;
        @NotNull
        private final String northEastLongitude;
        @NotNull
        private final String southWestLatitude;
        @NotNull
        private final String southWestLongitude;
    }

    private String language;

    @NotNull
    private LocalDate checkIn;
    @NotNull
    private LocalDate checkOut;
    private Integer shiftDays;

    private String destination;
    private Integer zone;

    @Singular
    private Set<Integer> matchingKeywords;
    private Matcher keywordsMatcher;

    // TODO improve name
    private Boolean dailyRate;

    private Boolean packaging;

    private Integer minCategory;
    private Integer maxCategory;

    @Singular
    private Set<Accommodation> ofTypes;

    @Singular
    private List<Integer> includeHotels;
    @Singular
    private List<Integer> excludeHotels;
    private Boolean useGiataCodes;

    // Limits
    @Min(value = 1)
    @Max(value = 1500)
    private Integer limitHotelsTo;
    @Min(value = 1)
    private Integer limitRoomsPerHotelTo;
    @Min(value = 0)
    private BigDecimal ratesHigherThan;
    @Min(value = 0)
    private BigDecimal ratesLowerThan;
    @Min(value = 1)
    private Integer limitRatesPerRoomTo;

    // Reviews
    @Min(value = 0)
    @Max(value = 5)
    @Digits(fraction = 1, integer = 1)
    private BigDecimal hbScoreHigherThan;
    @Min(value = 0)
    @Max(value = 5)
    @Digits(fraction = 1, integer = 1)
    private BigDecimal hbScoreLowerThan;
    private Integer numberOfHBReviewsHigherThan;
    //
    @Min(value = 0)
    @Max(value = 5)
    @Digits(fraction = 1, integer = 1)
    private BigDecimal tripAdvisorScoreHigherThan;
    @Min(value = 0)
    @Max(value = 5)
    @Digits(fraction = 1, integer = 1)
    private BigDecimal tripAdvisorScoreLowerThan;
    private Integer numberOfTripAdvisorReviewsHigherThan;

    // GeoLocation filters
    private DelimitedShape withinThis;

    @Singular
    private List<String> includeBoards;
    @Singular
    private List<String> excludeBoards;

    @Singular
    private List<String> includeRoomCodes;
    @Singular
    private List<String> excludeRoomCodes;

    @Singular
    private List<AvailRoom> rooms;

    Pay payed;

    public void validate() {

    }

    public AvailabilityRQ toAvailabilityRQ() {
        AvailabilityRQ availabilityRQ = new AvailabilityRQ();
        validate();
        //
        availabilityRQ.setLanguage(language);
        //
        availabilityRQ.setStay(new Stay(checkIn, checkOut, shiftDays, true));
        //
        availabilityRQ.setOccupancies(new ArrayList<>());
        for (AvailRoom room : rooms) {
            Occupancy occupancy = new Occupancy();
            occupancy.setAdults(room.getAdults());
            occupancy.setChildren(room.getChildren() != null ? room.getChildren() : 0);
            occupancy.setRooms(1);
            if (!CollectionUtils.isEmpty(room.getDetails())) {
                occupancy.setPaxes(new ArrayList<>());
                for (RoomDetail detail : room.getDetails()) {
                    Pax pax = new Pax();
                    pax.setType(detail.getType() == GuestType.ADULT ? HotelbedsCustomerType.AD : HotelbedsCustomerType.CH);
                    pax.setAge(detail.getAge());
                    pax.setName(detail.getName());
                    pax.setSurname(detail.getSurname());
                    occupancy.getPaxes().add(pax);
                }
            }
            availabilityRQ.getOccupancies().add(occupancy);
        }
        //
        if (withinThis != null) {
            GeoLocation geolocation = new GeoLocation();
            geolocation.setUnit(UnitMeasure.km);
            if (withinThis instanceof Circle) {
                Circle circle = (Circle) withinThis;
                geolocation.setLatitude(new BigDecimal(circle.getLatitude()));
                geolocation.setLongitude(new BigDecimal(circle.getLongitude()));
                geolocation.setRadius(new BigDecimal(circle.getRadiusInKilometers()));
            } else if (withinThis instanceof Square) {
                Square square = (Square) withinThis;
                geolocation.setLatitude(new BigDecimal(square.getNorthEastLatitude()));
                geolocation.setLongitude(new BigDecimal(square.getNorthEastLongitude()));
                geolocation.setSecondaryLatitude(new BigDecimal(square.getSouthWestLatitude()));
                geolocation.setSecondaryLongitude(new BigDecimal(square.getSouthWestLongitude()));
            }
            availabilityRQ.setGeolocation(geolocation);
        }
        //
        if (StringUtils.isNotBlank(destination)) {
            Destination dest = new Destination();
            dest.setCode(destination);
            if (zone != null) {
                dest.setZone(zone);
            }
            availabilityRQ.setDestination(dest);
        }
        //
        if (!CollectionUtils.isEmpty(matchingKeywords)) {
            List<Integer> keywords = new ArrayList<>();
            keywords.addAll(matchingKeywords);
            availabilityRQ.setKeywordsFilter(new KeywordsFilter(keywords, Matcher.ALL.equals(keywordsMatcher)));
        }
        //
        if (!CollectionUtils.isEmpty(includeHotels) && !CollectionUtils.isEmpty(excludeHotels)) {
            includeHotels.removeAll(excludeHotels);
        }
        if (!CollectionUtils.isEmpty(includeHotels) || !CollectionUtils.isEmpty(excludeHotels)) {
            HotelsFilter hotelsFilter = new HotelsFilter();
            if (!CollectionUtils.isEmpty(includeHotels)) {
                hotelsFilter.setIncluded(true);
                hotelsFilter.setHotels(includeHotels);
            } else {
                hotelsFilter.setIncluded(false);
                hotelsFilter.setHotels(excludeHotels);
            }
            hotelsFilter.setType(useGiataCodes != null && useGiataCodes ? HotelCodeType.GIATA : HotelCodeType.HOTELBEDS);
            availabilityRQ.setHotelsFilter(hotelsFilter);
        }
        //
        if (!CollectionUtils.isEmpty(includeBoards) || !CollectionUtils.isEmpty(excludeBoards)) {
            Boards boardFilter = new Boards();
            if (!CollectionUtils.isEmpty(includeBoards)) {
                boardFilter.setIncluded(true);
                boardFilter.setBoard(includeBoards);
            } else {
                boardFilter.setIncluded(false);
                boardFilter.setBoard(excludeBoards);
            }
            availabilityRQ.setBoards(boardFilter);
        }
        //
        //
        if (!CollectionUtils.isEmpty(includeRoomCodes) || !CollectionUtils.isEmpty(excludeRoomCodes)) {
            Rooms roomFilter = new Rooms();
            if (!CollectionUtils.isEmpty(includeRoomCodes)) {
                roomFilter.setIncluded(true);
                roomFilter.setRoom(includeRoomCodes);
            } else {
                roomFilter.setIncluded(false);
                roomFilter.setRoom(excludeRoomCodes);
            }
            availabilityRQ.setRooms(roomFilter);
        }
        //
        availabilityRQ.setDailyRate(dailyRate);
        //
        if (!CollectionUtils.isEmpty(ofTypes)) {
            List<Accommodation> types = new ArrayList<>();
            for (Accommodation type : ofTypes) {
                types.add(Accommodation.valueOf(type.name()));
            }
            availabilityRQ.setAccommodations(types);
        }
        List<ReviewFilter> reviewsFilter = null;
        if (hbScoreHigherThan != null || hbScoreLowerThan != null || numberOfHBReviewsHigherThan != null) {
            reviewsFilter = new ArrayList<>();
            ReviewFilter reviewFilter = new ReviewFilter();
            reviewFilter.setMaxRate(hbScoreLowerThan);
            reviewFilter.setMinRate(hbScoreHigherThan);
            reviewFilter.setMinReviewCount(numberOfHBReviewsHigherThan);
            reviewFilter.setType(ReviewsType.HOTELBEDS);
            reviewsFilter.add(reviewFilter);
        }
        if (tripAdvisorScoreHigherThan != null || tripAdvisorScoreLowerThan != null || numberOfTripAdvisorReviewsHigherThan != null) {
            if (reviewsFilter == null) {
                reviewsFilter = new ArrayList<>();
            }
            ReviewFilter reviewFilter = new ReviewFilter();
            reviewFilter.setMaxRate(tripAdvisorScoreLowerThan);
            reviewFilter.setMinRate(tripAdvisorScoreHigherThan);
            reviewFilter.setMinReviewCount(numberOfTripAdvisorReviewsHigherThan);
            reviewFilter.setType(ReviewsType.TRIPADVISOR);
            reviewsFilter.add(reviewFilter);
        }
        availabilityRQ.setReviewsFilter(reviewsFilter);
        //
        if (limitHotelsTo != null || maxCategory != null || minCategory != null || limitRoomsPerHotelTo != null || limitRatesPerRoomTo != null
            || ratesLowerThan != null || ratesHigherThan != null || packaging != null || payed != null) {

            Filter filter = new Filter();
            if (maxCategory != null) {
                filter.setMaxCategory(maxCategory);
            }
            if (minCategory != null) {
                filter.setMinCategory(minCategory);
            }
            if (packaging != null) {
                filter.setPackaging(packaging);
            }
            if (limitHotelsTo != null) {
                filter.setMaxHotels(limitHotelsTo);
            }
            if (limitRoomsPerHotelTo != null) {
                filter.setMaxRooms(limitRoomsPerHotelTo);
            }
            if (limitRatesPerRoomTo != null) {
                filter.setMaxRatesPerRoom(limitRatesPerRoomTo);
            }
            if (ratesLowerThan != null) {
                filter.setMaxRate(ratesLowerThan);
            }
            if (ratesHigherThan != null) {
                filter.setMinRate(ratesHigherThan);
            }
            if (payed != null) {
                switch (payed) {
                    case AT_HOTEL:
                        filter.setPaymentType(ShowDirectPayment.AT_HOTEL);
                        break;
                    case AT_WEB:
                        filter.setPaymentType(ShowDirectPayment.AT_WEB);
                        break;
                    case INDIFFERENT:
                        filter.setPaymentType(ShowDirectPayment.BOTH);
                        break;
                }
            }
            availabilityRQ.setFilter(filter);
        }
        //

        return availabilityRQ;
    }

    public static class AvailabilityBuilder {

        public AvailabilityBuilder addRoom(AvailRoomBuilder roomBuilder) {
            room(roomBuilder.build());
            return this;
        }

        public AvailabilityBuilder matchingAllKeywords() {
            keywordsMatcher(Matcher.ALL);
            return this;
        }

        public AvailabilityBuilder matchingAnyKeyword() {
            keywordsMatcher(Matcher.ANY);
            return this;
        }
    }
}
