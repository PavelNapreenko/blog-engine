package ru.pnapreenko.blogengine.repositories.calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@Transactional
public class CalendarRepository implements CalendarRepositoryInterface {

    private final EntityManager em;

    final static String WHERE = "WHERE p.isActive = 1 " +
            "AND p.moderationStatus = 'ACCEPTED' " +
            "AND p.time <= NOW() ";

    final static String WHERE_WITH_YEAR = "WHERE p.isActive = 1 " +
            "AND p.moderationStatus = 'ACCEPTED' " +
            "AND YEAR(p.time) = ";


    final String QUERY_YEARS = "SELECT DISTINCT YEAR(p.time) AS years " +
            "FROM Post p " +
            WHERE +
            "GROUP BY p.time " +
            "ORDER BY years";

    @Autowired
    public CalendarRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<Integer> findAllYears() {
        return em.createQuery((QUERY_YEARS), Integer.class).getResultList();
    }

    @Override
    public Map<String, Long> findAllPosts(String year) {
        return em.createQuery(getQuery(year), Tuple.class).getResultStream()
                .collect(
                        Collectors.toMap(
                                tuple -> tuple.get("post_date").toString(),
                                tuple -> (Long) tuple.get("num_posts"))
                );
    }

    private static String getQuery(String year) {
        return (year == null || year.isEmpty()) ?
                String.format("SELECT DISTINCT DATE_FORMAT(p.time,'%%Y-%%m-%%d') as post_date, COUNT(*) as num_posts FROM Post p %s GROUP BY p.time ",
                        WHERE) :
                String.format("SELECT DISTINCT DATE_FORMAT(p.time,'%%Y-%%m-%%d') as post_date, COUNT(*) as num_posts FROM Post p %s GROUP BY p.time ",
                        WHERE_WITH_YEAR + year);
    }
}
