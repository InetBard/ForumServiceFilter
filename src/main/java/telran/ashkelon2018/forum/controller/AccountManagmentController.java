package telran.ashkelon2018.forum.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import telran.ashkelon2018.forum.dto.UserProfileDTO;
import telran.ashkelon2018.forum.dto.UserRegDTO;
import telran.ashkelon2018.forum.service.AccountService;

@RestController
@RequestMapping("/account")
public class AccountManagmentController {
	@Autowired
	AccountService accountService;

	@PostMapping
	public UserProfileDTO register(@RequestBody UserRegDTO userRegDTO, 
			@RequestHeader("Authorization") String token) {
		return accountService.addUser(userRegDTO, token);
	}

	@PutMapping
	public UserProfileDTO update(@RequestBody UserRegDTO userRegDTO, 
			@RequestHeader("Authorization") String token) {
		return accountService.editUser(userRegDTO, token);
	}

	@DeleteMapping("{login}")
	public UserProfileDTO remove(@PathVariable String login, 
		@PathVariable String role,@RequestHeader("Authorization") String token) {
		return accountService.removeUser(login, token, role);
	}

	@PutMapping("/role/{login}/{role}")
	public Set<String> addRole(@PathVariable String login, @PathVariable String role,
			@RequestHeader("Authorization") String token) {
		return accountService.addRole(login, role, token);
	}

	@DeleteMapping("/role/{login}/{role}")
	public Set<String> removeRole(@PathVariable String login, @PathVariable String role,
			@RequestHeader("Authorization") String token) {
		return accountService.removeRole(login, role, token);
	}

	@PutMapping("/password")
	public void changePassword(@RequestHeader("X-Authorization") String password,
			@RequestHeader("Authorization") String token) {
		accountService.changePassword(password, token);
	}
	
	@GetMapping
	public UserProfileDTO loginUser(@RequestHeader("Authorization") String token) {
		return accountService.login(token);
	}
}

