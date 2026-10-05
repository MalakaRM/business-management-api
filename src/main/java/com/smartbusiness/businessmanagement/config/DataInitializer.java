package com.smartbusiness.businessmanagement.config;

import com.smartbusiness.businessmanagement.entity.Permission;
import com.smartbusiness.businessmanagement.entity.Role;
import com.smartbusiness.businessmanagement.entity.User;
import com.smartbusiness.businessmanagement.repository.PermissionRepository;
import com.smartbusiness.businessmanagement.repository.RoleRepository;
import com.smartbusiness.businessmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        // =========================
        // Permissions
        // =========================

        Permission userManage = createPermission(
                "USER_MANAGE",
                "Manage system users"
        );

        Permission productCreate = createPermission(
                "PRODUCT_CREATE",
                "Create products"
        );

        Permission productRead = createPermission(
                "PRODUCT_READ",
                "View products"
        );

        Permission productUpdate = createPermission(
                "PRODUCT_UPDATE",
                "Update products"
        );

        Permission inventoryRead = createPermission(
                "INVENTORY_READ",
                "View inventory"
        );

        Permission inventoryUpdate = createPermission(
                "INVENTORY_UPDATE",
                "Update inventory"
        );

        Permission orderCreate = createPermission(
                "ORDER_CREATE",
                "Create orders"
        );

        Permission orderRead = createPermission(
                "ORDER_READ",
                "View orders"
        );
        Permission orderUpdate = createPermission(
                "ORDER_UPDATE",
                "Update and confirm orders"
        );

        Permission orderDelete = createPermission(
                "ORDER_DELETE",
                "Cancel orders"
        );

        Permission reportRead = createPermission(
                "REPORT_READ",
                "View reports"
        );

        Permission purchaseCreate = createPermission(
                "PURCHASE_CREATE",
                "Create purchase records"
        );

        Permission purchaseRead = createPermission(
                "PURCHASE_READ",
                "View purchase records"
        );
        Permission purchaseUpdate = createPermission(
                "PURCHASE_UPDATE",
                "Update purchase records"
        );

        Permission purchaseDelete = createPermission(
                "PURCHASE_DELETE",
                "Cancel purchase records"
        );

        Permission auditRead = createPermission(
                "AUDIT_READ",
                "View audit logs"
        );
        Permission customerCreate = createPermission(
                "CUSTOMER_CREATE",
                "Create customers"
        );

        Permission customerRead = createPermission(
                "CUSTOMER_READ",
                "View customers"
        );

        Permission customerUpdate = createPermission(
                "CUSTOMER_UPDATE",
                "Update customers"
        );
        Permission roleManage = createPermission(
                "ROLE_MANAGE",
                "Manage system roles and permissions"
        );
        Permission categoryCreate = createPermission(
                "CATEGORY_CREATE",
                "Create categories"
        );

        Permission categoryRead = createPermission(
                "CATEGORY_READ",
                "View categories"
        );

        Permission categoryUpdate = createPermission(
                "CATEGORY_UPDATE",
                "Update and deactivate categories"
        );
        Permission supplierCreate = createPermission(
                "SUPPLIER_CREATE",
                "Create suppliers"
        );

        Permission supplierRead = createPermission(
                "SUPPLIER_READ",
                "View suppliers"
        );

        Permission supplierUpdate = createPermission(
                "SUPPLIER_UPDATE",
                "Update and deactivate suppliers"
        );


        // =========================
        // Roles
        // =========================

        Role adminRole = createRole(
                "ADMIN",
                "System administrator",
                Set.of(
                        userManage,
                        productCreate,
                        productRead,
                        productUpdate,
                        inventoryRead,
                        inventoryUpdate,
                        orderCreate,
                        orderRead,
                        orderUpdate,
                        orderDelete,
                        reportRead,
                        purchaseCreate,
                        purchaseRead,
                        purchaseUpdate,
                        purchaseDelete,
                        auditRead,
                        customerCreate,
                        customerRead,
                        customerUpdate,
                        roleManage,
                        categoryCreate,
                        categoryRead,
                        categoryUpdate,
                        supplierCreate,
                        supplierRead,
                        supplierUpdate


                )
        );

        createRole(
                "MANAGER",
                "Business manager",
                Set.of(
                        productRead,
                        productCreate,
                        productUpdate,
                        inventoryRead,
                        inventoryUpdate,
                        orderRead,
                        reportRead,
                        purchaseCreate,
                        purchaseRead,
                        auditRead,
                        customerCreate,
                        customerRead,
                        customerUpdate,
                        purchaseUpdate,
                        purchaseDelete,
                        supplierCreate,
                        supplierRead,
                        supplierUpdate
                )
        );

        createRole(
                "EMPLOYEE",
                "Business employee",
                Set.of(
                        productRead,
                        inventoryRead,
                        orderCreate,
                        orderRead,
                        customerRead
                )
        );

        // =========================
        // Default Admin User
        // =========================

        createAdminUser(adminRole);
    }

    private Permission createPermission(
            String name,
            String description
    ) {

        return permissionRepository
                .findByName(name)
                .orElseGet(() ->
                        permissionRepository.save(
                                Permission.builder()
                                        .name(name)
                                        .description(description)
                                        .build()
                        )
                );
    }

    private Role createRole(
            String name,
            String description,
            Set<Permission> permissions
    ) {

        Role role = roleRepository
                .findByName(name)
                .orElseGet(() ->
                        Role.builder()
                                .name(name)
                                .description(description)
                                .build()
                );

        /*
         * Important:
         * Even if the role already exists,
         * update its permissions.
         */
        role.setPermissions(permissions);

        return roleRepository.save(role);
    }

    private void createAdminUser(Role adminRole) {

        if (userRepository.existsByUsername("admin")) {
            return;
        }

        User admin = User.builder()
                .username("admin")
                .email("admin@smartbusiness.com")
                .password(
                        passwordEncoder.encode("Admin@123")
                )
                .enabled(true)
                .passwordChangeRequired(false)
                .roles(Set.of(adminRole))
                .build();

        userRepository.save(admin);
    }
}