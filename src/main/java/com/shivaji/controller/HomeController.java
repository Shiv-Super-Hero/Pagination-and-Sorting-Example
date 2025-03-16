package com.shivaji.controller;

import java.util.List;  
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.shivaji.entity.Products;
import com.shivaji.repository.ProductRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	
	@Autowired
	private ProductRepository productRepository;
	
	@GetMapping("/")
	public String home(Model m) {
		
//		List<Products> list = productRepository.findAll();
//		m.addAttribute("all_products",list);
		
		return findPaginationAndSorting(0,"id","asc", m);
	}
	
	@GetMapping("/page/{pageNo}")
	public String findPaginationAndSorting(@PathVariable(value="pageNo") int pageNo,
			@RequestParam(value="sortField") String sortField, @RequestParam(value="sortDir") String sortDir,
			Model m) {
		
		Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending():
			Sort.by(sortField).descending();
		
		
		Pageable pageable = PageRequest.of(pageNo, 3,sort);
		Page<Products> page = productRepository.findAll(pageable);
		
		List<Products> list = page.getContent();
		
		m.addAttribute("pageNo",pageNo);
		m.addAttribute("totalElements",page.getTotalElements());
		m.addAttribute("totalPages",page.getTotalPages());
		m.addAttribute("all_products",list);
		
		m.addAttribute("sortField",sortField);
		m.addAttribute("sortDir",sortDir);
		m.addAttribute("revSortDir",sortDir.equals("asc") ? "desc" : "asc");
		
		return "index";
	}
	
	
	@GetMapping("/load_form")
	public String loadForm() {
		return "add";
	}
	
	@GetMapping("/edit_form/{id}")
	public String editForm(@PathVariable(value="id") long id,Model m) {
		
		Optional<Products> product=productRepository.findById(id);
		
		Products pro = product.get();
		m.addAttribute("product",pro);
		
		return "edit";
	}
	
	@PostMapping("/save_products")
	public String saveProduct(@ModelAttribute Products products,HttpSession session) {
		
		productRepository.save(products);
		session.setAttribute("message", "Product Added Successfully...");
		return "redirect:/load_form";
	}
	
	@PostMapping("/update_products")
	public String updateProducts(@ModelAttribute Products products,HttpSession session) {
		
		productRepository.save(products);
		session.setAttribute("message", "Product Updated Successfully...");
		
		return "redirect:/";
	}
	
	@GetMapping("/delete/{id}")
	public String deleteProducts(@PathVariable(value="id") long id, HttpSession session) {
		
		productRepository.deleteById(id);
		session.setAttribute("message", "Product Deleted Successfully");
		
		return "redirect:/";
	}
	
}
