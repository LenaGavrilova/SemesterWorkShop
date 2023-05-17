package ru.kpfu.itis.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.kpfu.itis.models.Book;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepo extends JpaRepository<Book, Integer> {

    Optional<Book> findByTitle(String title);


    @Query(value = "select b from Book b where b.category.id= ?1 ")
    List<Book> findBookByCategory(int categoryId);


    @Query(value = "select b from Book b where b.category.id = ?4 and b.title like %?1%  and b.price >= ?2 and b.price <= ?3 order by b.price")
    List<Book> findBookByPartTitleAndCategoryAndPriceOrderByPriceAsc(String title, float min, float max, int category);


    @Query(value = "select b from Book b where b.category.id= ?4 and b.title like %?1% and b.price >= ?2 and b.price <= ?3 order by b.price desc")
    List<Book> findBookByPartTitleAndCategoryAndPriceOrderByPriceDesc(String title, float min, float max, int category);


    @Query(value = "select  b from Book b " +
            "where b.title like  %?1%    and  b.category.id=(select c.id from Category c where c.name=  ?2 )order by b.price")
    List<Book> findBookByPartTitleAndCategoryOrderByPriceAsc(String title, String category);

    @Query(value = "select b from Book b where b.title like %?1%  and  b.category.id=(select c.id from Category c where c.name= ?2 )order by b.price desc ")
    List<Book> findBookByPartTitleAndCategoryOrderByPriceDesc(String title, String category);

    @Query(value = "select b from Book b where b.category.id= ?1 and b.price>= ?2 and b.price<= ?3 order by b.price")
    List<Book> findBookByCategoryOrderByPriceAcs(int categoryId, float min, float max);

    @Query(value = "select b from Book b where b.category.id= ?1 and b.price>= ?2 and b.price<= ?3 order by b.price desc")
    List<Book> findBookByCategoryOrderByPriceDesc(int categoryId, float min, float max);
}
