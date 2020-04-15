# Protocol Outline

This document describes the design of the protocol that will be used by our application. It is a HTTP REST API. We will try to stay as RESTfull as possible. Any output is in JSON format.

Error messages adhere to the HTTP standard and contain a JSON body, as follows:

```
GET /asdf

404 Not Found
{
    error: "This endpoint does not exist"
}
```

## Protocol security

All messages are encoded in an HTTP-message which is sent over a secured connection to the server. This is an SSL-connection with a certificate from our certificate authority. Clients should distrust any certificates that come installed on the users devices and only trust our own certificate authority, whose certificate we embed in the application. This makes performing man-in-the-middle attacks on our application to analyze API-methods and API-keys a lot harder.

Furthermore, every HTTP-message has a special header field called "X-ApiKey" which contains a random 128-bit string encoded as hexadecimal numbers. This ApiKey is fixed for all instances of the application. While not perfect, this ApiKey will authenticate applications as being original, and not from a third party. This is used to prevent third parties from analyzing our API and writing alternative clients for our application.

To authenticate individual users, we use the basic HTTP authorization header. As username, we take the users e-mail address and as a password we take the hashed password of the user. The password is hashed 65536 times with SHA256. If no user credentials are available (yet), the authorization header is left out. This will however have impact on the API functionality that is available, as we cannot authenticate the user.

For example, a request to the server may look like:
```
GET /user HTTP/1.1
Host: compsci.gq
X-ApiKey: 83a164f6fb110a95489542b81ab25441
Authorization: Basic dXNlckBlbWFpbC5jb206NDQ3NjFjMTgxOTU5ZjczNjQ1NGU1M2JkNz
```

When an unauthenticated user or an user who has no rights to view an endpoint tries to access that endpoint, the API should return 403 "Forbidden" with an error message specifying the precise error. Example:

```
GET /user

403 Forbidden
{
    error: "Combination of e-mail address and password not correct."
}
```
### Get user info

```
GET /user

Returns:
{
    userid: 5132,
    username: "exampleuser",
    emailaddress: "user@email.com",
    accountCreated: 1551721011,
    userconfig: {
        meal: "LowMeatEater",
        typeOfHouse: "CornerHouse",
        peopleInHouse: 2,
        solarPanels: 0,
        houseTemperature: 20.1
    }
}
```


### Get balance for user

```
GET /balance

Returns:
{
    value: 5213
}
```

### Withdraw money

```
PUT /withdraw

{
    value: 21.5
}

Returns:
{
    current balance?
}
```