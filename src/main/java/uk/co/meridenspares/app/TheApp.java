package uk.co.meridenspares.app;

import java.util.List;

import org.joda.time.DateTime;
import org.springframework.context.support.GenericXmlApplicationContext;

import uk.co.meridenspares.domain.AppUser;
import uk.co.meridenspares.domain.Contact;
import uk.co.meridenspares.domain.Model;
import uk.co.meridenspares.domain.Section;
import uk.co.meridenspares.persistence.dao.api.AppUserDao;
import uk.co.meridenspares.persistence.dao.api.ContactDao;
import uk.co.meridenspares.persistence.dao.api.ModelDao;
import uk.co.meridenspares.persistence.dao.api.exception.DaoException;
import uk.co.meridenspares.service.api.ContactService;
import uk.co.meridenspares.service.api.exception.MsServiceException;

public class TheApp {

	public TheApp() {
		GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
		ctx.load("classpath:ms-app-context.xml");
		ctx.refresh();
		
		System.out.println("App context initialized successfully");
		
		AppUser user = new AppUser();
		user.setName("Bob");
		user.setPassword("password");
		
		Contact contact = new Contact();
		contact.setName("Bob");
		contact.setDescription("Customer");
		DateTime dt = new DateTime();
		contact.setCreatedDate(dt);
		contact.setLastContactedDate(dt);

		AppUserDao appUserDao = (AppUserDao) ctx.getBean("appUserDao");
		ContactDao contactDao = (ContactDao) ctx.getBean("contactDao");
		ModelDao modelDao = (ModelDao) ctx.getBean("modelDao");
		
		ContactService contactService = (ContactService) ctx.getBean("contactService");
		
		try {
			appUserDao.create(user);
			contactDao.create(contact);
			
			System.out.println("Model years:");
//			List<Integer> years = modelDao.getModelYears();
//			for (Integer year : years) {
//				System.out.println(year);
//			}
			
			contact.setName("Jim");
			contactService.save(contact);
			
			List<Contact> contacts = contactService.findAll();
			for (Contact c : contacts) {
				System.out.println("Contact: " + c.getName());
			}
			
			List<Model> models = modelDao.getModelsForYear(1980);
			for (Model model : models) {
				List<Section> sections = model.getSections();
				for (Section section : sections) {
					System.out.println(section.getTitle());
				}
			}
		}
		catch (MsServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
/*		
		CategoryService categoryService = ctx.getBean("categoryService", CategoryService.class);
		
		List<Category> categories = categoryService.findAllParentCategory();
		
		for (Category category: categories) {
			System.out.println("Category id: " + category.getCategoryId());
			List<Category> subCategories = categoryService.findAllSubCategory(category.getCategoryId());
			for (Category subCategory: subCategories) {
				System.out.println("Sub-category id: " + subCategory.getCategoryId());
			}
		}
*/

		System.out.println("App completed successfully");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		TheApp theApp = new TheApp();
	}

}
