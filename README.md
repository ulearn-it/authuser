# Authuser

Service responsible for authentication and user management.

## Routes
Down below are the application routes

###  :curly_haired_woman: User

#### [GET] /users
Returns a page result with users. Admin route.

#### [GET] /{userId}
Returns a user filtered by a unique id. Route authorized for admins, instructors and for student if checking their own data.

#### [DELETE] /{userId}
Removes the user with identifier equals to userId. 

#### [PUT] /{userId}
Updates the user with identifier equals to userId.
<br /> Body: 
```
{
    fullName: 'John Paul Jones',
    phoneNumber: '1234-4321',
    cpf: '12345678-90'
}
```

#### [PUT] /{userId}/password
Updates the password from user with identifier equals to userId.
<br />Body:
```
{
    oldPassword: '123456',
    password: 'A@ka1!',
}
```

#### [PUT] /{userId}/image
Updates the image from user with identifier equals to userId.
<br />Body:
```
{
    imageUrl: 'https://image',
}
```
