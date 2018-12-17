package telran.ashkelon2018.forum.service;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.util.Set;


import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.stereotype.Service;

import telran.ashkelon2018.forum.configuration.AccountConfiguration;
import telran.ashkelon2018.forum.configuration.AccountUserCredentials;
import telran.ashkelon2018.forum.dao.RolesRepository;
import telran.ashkelon2018.forum.dao.UserAccountRepository;
import telran.ashkelon2018.forum.domain.Role;
import telran.ashkelon2018.forum.domain.UserAccount;
import telran.ashkelon2018.forum.dto.NewRoleDTO;
import telran.ashkelon2018.forum.dto.UserProfileDTO;
import telran.ashkelon2018.forum.dto.UserRegDTO;
import telran.ashkelon2018.forum.exceptions.UserConflictException;

@Service
public class AccountServiceImpl implements AccountService {
	@Autowired
	UserAccountRepository userRepository;
	@Autowired
	RolesRepository rolesRepository;
	@Autowired
	AccountConfiguration accountConfiguration;

	@Override
	public UserProfileDTO addUser(UserRegDTO userRegDto, String token) {
		AccountUserCredentials credentials = accountConfiguration.tokenDecode(token);
		System.err.println("Start service");
		if (userRepository.existsById(credentials.getLogin())) {
			throw new UserConflictException();
		}
		String hashPassword = BCrypt.hashpw(credentials.getPassword(), BCrypt.gensalt());
		UserAccount userAccount = UserAccount.builder()
				.login(credentials.getLogin())
				.password(hashPassword)
				.firstName(userRegDto.getFirstName())
				.lastName(userRegDto.getLastName())
				.role("User")
				.expdate(LocalDateTime.now().plusDays(accountConfiguration.getExpPeriod()))
				.build();
		userRepository.save(userAccount);
		return convertToUserProfileDto(userAccount);
	}
	private UserProfileDTO convertToUserProfileDto(UserAccount userAccount) {
		return UserProfileDTO.builder()
				.firstName(userAccount.getFirstName())
				.lastName(userAccount.getLastName())
				.login(userAccount.getLogin())
				.roles(userAccount.getRoles())
				.build();
	}

	@Override
	public UserProfileDTO editUser(UserRegDTO userRegDto, String token) {
		AccountUserCredentials credentials = accountConfiguration.tokenDecode(token);
		UserAccount userAccount = userRepository.findById(credentials.getLogin()).get();
		if (userRegDto.getFirstName()!= null) {
			userAccount.setFirstName(userRegDto.getFirstName());
		}
		if (userRegDto.getLastName()!= null) {
			userAccount.setLastName(userRegDto.getLastName());
		}
		userRepository.save(userAccount);
		return convertToUserProfileDto(userAccount);
	}

	@Override
	public UserProfileDTO removeUser(String login, String token, String role) {
		//FIXME найти юзеаккаунт если его логин такой и роль такая то тогда ок
		AccountUserCredentials credentials = accountConfiguration.tokenDecode(token);
		UserAccount whoDelete = userRepository.findById(credentials.getLogin()).get();
		Set<String> roles = whoDelete.getRoles();
		if ((rolesRepository.findAllById(whoDelete.getRoles())!=null && whoDelete.getLogin().equals(login))
				||(whoDelete.getRoles().contains("admin")||whoDelete.getRoles().contains("Moderator"))){
				UserAccount userAccount = userRepository.findById(login).get();
				if (userAccount!=null) {
					userRepository.delete(userAccount);
				}
				return convertToUserProfileDto(userAccount);
			}			
		return null;
	}
	
	//Method response for creating a new roles in collection Roles in DataBase
	//depend at Admin rules and rights
	public Role addRoleToDB(NewRoleDTO nameRole) {
//		Role role = rolesRepository.findById(nameRole.getRole()).orElse(null);
		System.err.println(nameRole.getRole());
//		if (role==null) {
			Role newRole = new Role(nameRole.getRole());
			return rolesRepository.save(newRole);	
//		}
//		return rolesRepository.save(role);
	}

	@Override
	public Set<String> addRole(String login, String role, String token) {
		AccountUserCredentials credentials = accountConfiguration.tokenDecode(token);
		UserAccount whoAdd = userRepository.findById(credentials.getLogin()).get();
		if (whoAdd.getRoles().contains("Admin")) {
			UserAccount userAccount = userRepository.findById(login).get();
			if (userAccount!=null) {
				userAccount.addRole(role);
				userRepository.save(userAccount);
			}else {
				return null;
			}
			return userAccount.getRoles();
		}
		return null;
	}

	@Override
	public Set<String> removeRole(String login, String role, String token) {
		UserAccount userAccount = userRepository.findById(login).get();
		if (userAccount!=null) {
			userAccount.removeRole(role);
			userRepository.save(userAccount);
		}else {
			return null;
		}
		return userAccount.getRoles();
	}

	@Override
	public void changePassword(String password, String token) {
		
		AccountUserCredentials credentials = accountConfiguration.tokenDecode(token);
		UserAccount userAccount = userRepository.findById(credentials.getLogin()).get();
		String hashPassword = BCrypt.hashpw(password, BCrypt.gensalt());
		userAccount.setPassword(hashPassword);
		userAccount.setExpdate(LocalDateTime.now().plusDays(accountConfiguration.getExpPeriod()));
		userRepository.save(userAccount);
	}
	@Override
	public UserProfileDTO login(String token) {
		AccountUserCredentials credentials = accountConfiguration.tokenDecode(token);
		UserAccount userAccount = userRepository.findById(credentials.getLogin()).get();
		return convertToUserProfileDto(userAccount);
		
	}
		
}
