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

    const response = await fetch(`signIn`, requestOptions);

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

    const response = await fetch(`user`, requestOptions);

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