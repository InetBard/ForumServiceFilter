package telran.ashkelon2018.forum.service;

import java.util.Set;

import telran.ashkelon2018.forum.domain.Role;
import telran.ashkelon2018.forum.dto.NewRoleDTO;
import telran.ashkelon2018.forum.dto.UserProfileDTO;
import telran.ashkelon2018.forum.dto.UserRegDTO;

public interface AccountService {
	UserProfileDTO addUser(UserRegDTO userRegDto, String token);

	UserProfileDTO editUser(UserRegDTO userRegDto, String token);

	UserProfileDTO removeUser(String login,String token, String role);

	UserProfileDTO login(String token);
	
	Set<String> addRole(String login, String role, String token);

	Set<String> removeRole(String login, String role, String token);
	
	void changePassword(String password, String token);
	
	Role addRoleToDB(NewRoleDTO nameRole);
//	метод логин возвр юрездто 
//	2) Сделать фильтр защищающий всё что начинается с форум и роме посл 3
}
