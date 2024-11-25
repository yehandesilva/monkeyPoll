/*
Login user given email and password.
Return:
- Success: True if user is allowed to log in
- Body - Fetched user in JSON format or message describing the error
*/
export const loginUser = async (email, password) => {

    const requestOptions = {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({email: email, password: password}),
    };

    const response = await fetch(`login`, requestOptions);

    const status = {
        success: false,
        body: {
            message: "Failed to login"
        }
    }

    if (response.ok) {
        status.success = true;
        status.body = await response.json();
    }

    return status;
}

/*
Create user given first name, last name, email and password.
Return:
- Success: True if user is created
- Body - Created user in JSON format or message describing the error
*/
export const registerUser = async (firstName, lastName, email, password) => {

    const requestOptions = {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({firstName: firstName, lastName: lastName, email: email, password: password}),
    };

    const response = await fetch(`register`, requestOptions);

    const status = {
        success: false,
        body: {
            message: "Failed to create account"
        }
    }

    if (response.ok) {
        status.success = true;
        status.body = await response.json();
    }

    return status;
}

/*
Logout the user that is currently logged in.
Return:
- Success: True if user has been successfully logged out on the server side
- Body - No content (204) on success or message describing the error
*/
export const logoutUser = async () => {

    const requestOptions = {
        method: 'POST',
    };

    const response = await fetch(`logout`, requestOptions);

    const status = {
        success: false,
        body: {
            message: "Failed to logout user"
        }
    }

    if (response.ok) {
        status.success = true;
    }

    return status;
}

// Get user
// Return:
// - Success: True if user is successfully fetched
// - Body - Fetched user in JSON format or message describing the error
export const getUser = async () => {

    const requestOptions = {
        method: 'GET',
        headers: {'Content-Type': 'application/json'},
    };

    const response = await fetch(`user`, requestOptions);

    const status = {
        success: false,
        body: {
            message: "Failed to get user"
        }
    }

    if (response.ok) {
        status.success = true;
        status.body = await response.json();
    }

    return status;
}