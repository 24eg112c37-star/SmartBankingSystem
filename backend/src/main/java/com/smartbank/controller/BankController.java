package com.smartbank.controller;
import com.smartbank.service.BankingService; import com.smartbank.service.AccountSecurityService; import jakarta.validation.Valid; import jakarta.validation.constraints.*; import org.springframework.security.core.Authentication; import org.springframework.web.bind.annotation.*; import java.math.BigDecimal; import java.util.*;
@RestController @RequestMapping("/api") public class BankController {
 private final BankingService service; private final AccountSecurityService security; public BankController(BankingService s,AccountSecurityService security){service=s;this.security=security;}
 public record Money(@NotNull @DecimalMin("0.01") @Digits(integer=7,fraction=2) BigDecimal amount,@Size(max=255) String description){}
 public record Transfer(@NotNull @DecimalMin("0.01") @Digits(integer=7,fraction=2) BigDecimal amount,@NotBlank String recipientAccount,@Size(max=255) String description){}
 public record Profile(@NotBlank @Size(max=120) String fullName,@NotBlank @Size(max=30) String phone){}
 public record PasswordChange(@NotBlank String currentPassword,@NotBlank @Pattern(regexp="^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,72}$",message="Use 8+ characters with uppercase, lowercase, and a number.") String newPassword){}
 @GetMapping("/me") public Map<String,Object> me(Authentication a){var u=service.user(a.getName());return Map.of("id",u.id,"name",u.fullName,"email",u.email,"phone",u.phone,"role",u.role);}
 @PutMapping("/me") public Object updateMe(Authentication a,@Valid @RequestBody Profile p){return service.updateProfile(a.getName(),p.fullName(),p.phone());}
 @PutMapping("/me/password") public Object changePassword(Authentication a,@Valid @RequestBody PasswordChange p){security.changePassword(a.getName(),p.currentPassword(),p.newPassword());return Map.of("message","Password updated successfully.");}
 @GetMapping("/accounts") public Object accounts(Authentication a){return service.myAccounts(a.getName());}
 @GetMapping("/transactions") public Object tx(Authentication a){return service.myTransactions(a.getName());}
 @GetMapping("/notifications") public Object notifications(Authentication a){return service.myNotifications(a.getName());}
 @PatchMapping("/notifications/{id}/read") public Object markRead(Authentication a,@PathVariable Long id){service.markNotificationRead(a.getName(),id);return Map.of("message","Notification marked as read.");}
 @PostMapping("/accounts/{id}/deposit") public Object deposit(Authentication a,@PathVariable Long id,@Valid @RequestBody Money m){return service.deposit(a.getName(),id,m.amount(),m.description());}
 @PostMapping("/accounts/{id}/withdraw") public Object withdraw(Authentication a,@PathVariable Long id,@Valid @RequestBody Money m){return service.withdraw(a.getName(),id,m.amount(),m.description());}
 @PostMapping("/accounts/{id}/transfer") public Object transfer(Authentication a,@PathVariable Long id,@Valid @RequestBody Transfer m){return service.transfer(a.getName(),id,m.recipientAccount(),m.amount(),m.description());}
 @GetMapping("/admin/summary") public Object summary(){return service.adminStats();}
 @GetMapping("/admin/customers") public Object customers(){return service.adminUsers();}
 @PatchMapping("/admin/customers/{id}/status") public Object status(@PathVariable Long id,@RequestBody Map<String,Boolean> body){service.setActive(id,Boolean.TRUE.equals(body.get("active")));return Map.of("message","Customer status updated.");}
 @GetMapping("/admin/transactions") public Object allTransactions(){return service.allTransactions();}
}
