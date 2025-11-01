package com.theezyArt.theezyArtPortfolio.services;

import com.theezyArt.theezyArtPortfolio.data.model.Admin;
import com.theezyArt.theezyArtPortfolio.data.repositories.AdminRepository;
import com.theezyArt.theezyArtPortfolio.dto.request.AdminLoginRequest;
import com.theezyArt.theezyArtPortfolio.dto.response.AdminLoginResponse;
import com.theezyArt.theezyArtPortfolio.utils.exceptions.AdminNotFoundException;
import com.theezyArt.theezyArtPortfolio.utils.passwordEncoder.PasswordHashingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService, UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private JWTService jwtService;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        return adminRepository.findAdminByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found"));
    }

    @Override
    public AdminLoginResponse login(AdminLoginRequest adminLoginRequest) {
        Admin foundAdmin = adminRepository.findAdminByEmail(adminLoginRequest.getEmail())
                .orElseThrow(() -> new AdminNotFoundException("Invalid Email"));

        boolean isPasswordCorrect = PasswordHashingService.checkPassword(adminLoginRequest.getPassword(), foundAdmin.getPassword());
        if (!isPasswordCorrect){
            throw new AdminNotFoundException("Invalid credentials");
        }
        String accessToken = jwtService.generateAccessToken((UserDetails) foundAdmin);

        AdminLoginResponse adminLoginResponse = modelMapper.map(foundAdmin, AdminLoginResponse.class);
        adminLoginResponse.setMessage("Login successful");
        adminLoginResponse.setAccessToken(accessToken);
        return adminLoginResponse;
    }

    @Override
    public void register() {
        if (adminRepository.findAdminByEmail(adminEmail).isEmpty()){
            Admin permanentAdmin = new Admin();
            permanentAdmin.setEmail(adminEmail);
            permanentAdmin.setPassword(PasswordHashingService.hashPassword(adminPassword));
            permanentAdmin.setRole("ROLE_ADMIN");
            adminRepository.save(permanentAdmin);
            System.out.println((String.valueOf("Admin Registered successfully with email: " + permanentAdmin.getEmail())));
            System.out.println("Admin password: " + permanentAdmin.getPassword());
        }
    }
}
