# Authentication Filter Documentation

## Overview
The `AuthenticationFilter` is a container request filter that handles authentication for API endpoints using JWT tokens.

## Annotation Inheritance and Priority

### Annotation Lookup Mechanism
The filter uses a two-step lookup process for authentication annotations:
1. **Method-Level Annotation**: First, it checks for an `@Authentication` annotation on the specific method being called.
2. **Class-Level Annotation**: If no method-level annotation is found, it falls back to checking the class-level `@Authentication` annotation.

### Precedence Rules

#### Case 1: Method-Level and Class-Level Annotations
```kotlin
@Authentication(roles = [Role.ADMIN])  // Class-level annotation
class UserController {
    @Authentication(roles = [Role.PATIENT])  // Method-level annotation
    fun specificMethod() {  }
}
```
- For `specificMethod()`, only the **method-level annotation takes precedence**
- Only users with the `PATIENT` role can access this method
- The class-level `ADMIN` role is completely overridden

#### Case 2: Only Class-Level Annotation
```kotlin
@Authentication(roles = [Role.ADMIN])  // Class-level annotation
class UserController {
    fun methodWithoutAnnotation() {  }
}
```
- For `methodWithoutAnnotation()`, the class-level `ADMIN` role applies
- Only administrators can access this method

#### Case 3: Only Method-Level Annotation
```kotlin
class UserController {
    @Authentication(roles = [Role.PATIENT])  // Method-level annotation
    fun specificMethod() {  }
}
```
- For `specificMethod()`, only `PATIENT` role is allowed
- No default access is granted if no class-level annotation exists

### Role Inheritance Behavior
- **No Automatic Role Inheritance**: A method-level annotation completely replaces a class-level annotation
- Prevents unintended access due to implicit role inheritance

## Security Considerations

### Token Validation Process
1. Check for access token in cookies
2. Validate token against allowed roles
3. If token is invalid, attempt token refresh
4. Deny access if refresh fails

### Token Refresh Mechanism
- Attempts to generate a new access token using a valid refresh token
- Prevents unnecessary re-authentication for short-lived token expiration

## Recommended Usage Patterns

### Explicit Role Definition
```kotlin
@Authentication(roles = [Role.ADMIN, Role.HCP])  // Multiple roles allowed
class AdminController {
    @Authentication(roles = [Role.ADMIN])  // More restrictive method-level
    fun sensitiveAdminMethod() {  }
}
```

### Flexible Access Control
- Use class-level annotations for default access levels
- Use method-level annotations for fine-grained permissions
- Clearly document the intended access strategy
