// Get survey given its id.
// Return:
// - Success: True if survey is successfully fetched
// - Body - Fetched survey in JSON format or message describing the error
export const getSurvey = async (id) => {

    const requestOptions = {
        method: 'GET',
        headers: {'Content-Type': 'application/json'},
    };

    const response = await fetch(`survey/${id}`, requestOptions);

    const status = {
        success: false,
        body: {
            message: "Failed to get survey"
        }
    }

    if (response.ok) {
        status.success = true;
        status.body = await response.json();
    } else if (response.status === 403) {
        status.body = await response.json();
    } else if (response.status === 404) {
        status.body = await response.json();
    }

    return status;
}

// Submits the completed survey.
// Return:
// - Success: True if survey is successfully fetched
export const submitSurvey = async (surveyId, email, responses) => {
    // Create a new map with the following structure:
    /**
     * {
     *     'email': 'someemail@gmail.com',
     *     'responses': [
     *         {
     *             'questionId': <questionId>,
     *             'response': <response>,
     *             'type': <type>
     *         },
     *         ...
     *     ]
     * }
     */
        // Convert responses map to Array and structure as above
    const responsesArr = Array.from(responses, ([questionId, response]) => {
            let type;
            if (typeof response === 'string') {
                type = 'text';
            } else if (typeof response === 'number') {
                type = 'number';
            } else if (typeof response === 'object' && response.choice) {
                type = 'choice';
                response = response.choice;
            }
            return { questionId, response, type };
        });

    const requestOptions = {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email: email, responses: responsesArr }),
    };

    // Send request and await response from server
    const response = await fetch(`survey/${surveyId}`, requestOptions);
    const status = {
        success: false,
        body: {
            message: "Failed to submit survey response"
        }
    };
    if (response.ok) {
        status.success = true;
        status.body = await response.json();
    } else {
        const errorResponse = await response.json();
        switch (response.status) {
            case 409:
                status.body.message = "This email has already submitted the survey!";
                break;
            default:
                status.body.message = errorResponse.message || status.body.message;
                break;
        }
    }

    // Return response status
    return status;
};

// Create a new survey given its contents
// Return:
// - Success: True if survey is successfully created
// - Body - Created survey ID (at minimum) in JSON format or message describing the error
export const createSurvey = async (surveyContents) => {

    const requestOptions = {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(surveyContents)
    };

    const response = await fetch("/user/survey", requestOptions);

    const status = {
        success: false,
        body: {
            message: "Failed to create survey"
        }
    }

    if (response.ok) {
        status.success = true;
        status.body = await response.json();
    }

    return status;
}

// Get a survey's responses
// Return:
// - Success: True if survey responses are retrived
// - Body - The survey responses in JSON format or message describing the error
export const getSurveyResponses = async (surveyId) => {

    const requestOptions = {
        method: 'GET',
    };

    const response = await fetch(`/user/survey/${surveyId}/responses`, requestOptions);

    const status = {
        success: false,
        body: {
            message: "Failed to get survey responses"
        }
    }

    if (response.ok) {
        status.success = true;
        status.body = await response.json();
    }

    return status;
}

// Get a survey's results
// Return:
// - Success: True if survey results are retrieved
// - Body - The survey results in JSON format or message describing the error
export const getSurveyResults = async (surveyId) => {

    const requestOptions = {
        method: 'GET',
    };

    const response = await fetch(`/user/survey/${surveyId}/results`, requestOptions);

    const status = {
        success: false,
        body: {
            message: "Failed to get survey results"
        }
    }

    if (response.ok) {
        status.success = true;
        status.body = await response.json();
    }

    return status;
}

// Close a survey
// Return:
// - Success: True if survey has been closed
// - Body - No content (204) on success or message describing the error
export const postCloseSurvey = async (surveyId) => {

    const requestOptions = {
        method: 'POST',
    };

    const response = await fetch(`/user/survey/${surveyId}/close`, requestOptions);

    const status = {
        success: false,
        body: {
            message: "Failed to close survey"
        }
    }

    if (response.ok) {
        status.success = true;
    }

    return status;
}

// Get AI generated prompts for survey questions
// Return:
// - Success: True if prompts are generated
// - Body - The questions in JSON format or message describing the error
export const getAiQuestions = async (prompt) => {

    const requestOptions = {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({message: prompt})
    };

    const response = await fetch(`/user/ai/generate`, requestOptions);

    const status = {
        success: false,
        body: {
            message: "Failed to get prompts"
        }
    }

    if (response.ok) {
        status.success = true;
        status.body = await response.json();
    } else {
        // Check for 503 Service Unavailable response from backend (via Hystrix fallback method)
        if (response.status === 503) {
            status.body.message = "Uncle Bob is currently unavailable. Please try again later";
        } else {
            status.body.message = "Uncle Bob had some trouble generating questions for you. Can you try again?";
        }
    }

    return status;
}

