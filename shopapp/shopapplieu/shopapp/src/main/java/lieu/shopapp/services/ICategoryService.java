package lieu.shopapp.services;

import lieu.shopapp.dtos.CategoryDTO;
import lieu.shopapp.models.Category;

import java.util.List;

public interface ICategoryService {
    Category createCategory(CategoryDTO categoryDTO);
    Category updateCategory(long categoryId,CategoryDTO categoryDTO);
    void deleteCategory(long id);
    List<Category> getAllCategories();
    Category getCategoryById(long id);
}
