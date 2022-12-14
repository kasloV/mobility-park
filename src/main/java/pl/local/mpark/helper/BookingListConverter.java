package pl.local.mpark.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import pl.local.mpark.entity.Booking;
import pl.local.mpark.service.BookingService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BookingListConverter implements Converter<String[], Set<Booking>> {

    private final BookingService bookingService;

    @Autowired
    public BookingListConverter(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Override
    public Set<Booking> convert(String[] s) {
        Set<Booking> bookings = new HashSet<Booking>();
        for(String id : s) {
            Booking booking = this.bookingService.getBooking(Long.parseLong(id));
            if(booking == null) return null;
            bookings.add(booking);
        }
        return bookings;
    }
}
