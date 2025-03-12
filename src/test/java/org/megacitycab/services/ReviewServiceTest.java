package org.megacitycab.services;

import org.junit.Before;
import org.junit.Test;
import org.megacitycab.service.customer.ReviewService;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.megacitycab.dao.ReviewDAO;
import org.megacitycab.model.Review;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReviewServiceTest {

    private ReviewService reviewService;

    @Mock
    private ReviewDAO reviewDAOMock;

    @Before
    public void setUp() throws Exception {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);
        reviewService = new ReviewService();

        // Use reflection to inject the mock ReviewDAO into ReviewService
        Field daoField = reviewService.getClass().getDeclaredField("reviewDAO");
        daoField.setAccessible(true);
        daoField.set(reviewService, reviewDAOMock);
    }

    @Test
    public void testAddReview_withNullDate() {
        Review review = new Review();
        review.setReviewDate(null);

        // Simulate a successful DAO call
        when(reviewDAOMock.addReview(any(Review.class))).thenReturn(true);

        boolean result = reviewService.addReview(review);
        assertTrue(result);
        // Verify that the review date was set (i.e., not null anymore)
        assertNotNull(review.getReviewDate());
        verify(reviewDAOMock, times(1)).addReview(review);
    }

    @Test
    public void testAddReview_withException() {
        Review review = new Review();
        review.setReviewDate(new Date());

        // Simulate an exception from the DAO
        when(reviewDAOMock.addReview(any(Review.class))).thenThrow(new RuntimeException("DB error"));

        boolean result = reviewService.addReview(review);
        assertFalse(result);
    }

    @Test
    public void testUpdateReview_success() {
        Review review = new Review();
        when(reviewDAOMock.updateReview(review)).thenReturn(true);

        boolean result = reviewService.updateReview(review);
        assertTrue(result);
        verify(reviewDAOMock, times(1)).updateReview(review);
    }

    @Test
    public void testUpdateReview_exception() {
        Review review = new Review();
        when(reviewDAOMock.updateReview(review)).thenThrow(new RuntimeException("DB error"));

        boolean result = reviewService.updateReview(review);
        assertFalse(result);
    }

    @Test
    public void testDeleteReview_success() {
        int reviewID = 1;
        when(reviewDAOMock.deleteReview(reviewID)).thenReturn(true);

        boolean result = reviewService.deleteReview(reviewID);
        assertTrue(result);
        verify(reviewDAOMock, times(1)).deleteReview(reviewID);
    }

    @Test
    public void testDeleteReview_exception() {
        int reviewID = 1;
        when(reviewDAOMock.deleteReview(reviewID)).thenThrow(new RuntimeException("DB error"));

        boolean result = reviewService.deleteReview(reviewID);
        assertFalse(result);
    }

    @Test
    public void testGetReviewById_success() {
        int reviewID = 1;
        Review review = new Review();
        when(reviewDAOMock.getReviewById(reviewID)).thenReturn(review);

        Review result = reviewService.getReviewById(reviewID);
        assertNotNull(result);
        verify(reviewDAOMock, times(1)).getReviewById(reviewID);
    }

    @Test
    public void testGetReviewById_exception() {
        int reviewID = 1;
        when(reviewDAOMock.getReviewById(reviewID)).thenThrow(new RuntimeException("DB error"));

        Review result = reviewService.getReviewById(reviewID);
        assertNull(result);
    }

    @Test
    public void testGetReviewsByUserId_success() {
        int userID = 1;
        List<Review> reviews = Arrays.asList(new Review(), new Review());
        when(reviewDAOMock.getReviewsByUserId(userID)).thenReturn(reviews);

        List<Review> result = reviewService.getReviewsByUserId(userID);
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(reviewDAOMock, times(1)).getReviewsByUserId(userID);
    }

    @Test
    public void testGetReviewsByUserId_exception() {
        int userID = 1;
        when(reviewDAOMock.getReviewsByUserId(userID)).thenThrow(new RuntimeException("DB error"));

        List<Review> result = reviewService.getReviewsByUserId(userID);
        assertNull(result);
    }

    @Test
    public void testGetAllReviews() {
        List<Review> reviews = Arrays.asList(new Review(), new Review(), new Review());
        when(reviewDAOMock.getAllReviews()).thenReturn(reviews);

        List<Review> result = reviewService.getAllReviews();
        assertNotNull(result);
        assertEquals(3, result.size());
        verify(reviewDAOMock, times(1)).getAllReviews();
    }

    @Test
    public void testGetReviewsByRating() {
        int rating = 5;
        List<Review> reviews = Arrays.asList(new Review());
        when(reviewDAOMock.getReviewsByRating(rating)).thenReturn(reviews);

        List<Review> result = reviewService.getReviewsByRating(rating);
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(reviewDAOMock, times(1)).getReviewsByRating(rating);
    }

    @Test
    public void testGetReviewsByDriverID() {
        int driverID = 2;
        List<Review> reviews = Arrays.asList(new Review(), new Review());
        when(reviewDAOMock.getReviewsByDriverID(driverID)).thenReturn(reviews);

        List<Review> result = reviewService.getReviewsByDriverID(driverID);
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(reviewDAOMock, times(1)).getReviewsByDriverID(driverID);
    }
}
