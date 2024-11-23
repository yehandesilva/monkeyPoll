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
        console.log(status.body);
    }

    return status;
}

// Submits the completed survey.
// Return:
// - Success: True if survey is successfully fetched
export const submitSurvey = async(surveyId, responses) => {
    // Create a new map with the following structure:
    /**
     * {
     *     'responses': [
     *         {
     *             'questionId': <questionId>,
     *             'response': <response>
     *         },
     *         {
     *             'questionId': <questionId>,
     *             'response': <response>
     *         },
     *         ...
     *     ]
     * }
     */
    // Convert responses map to Array and structure as above
    const responsesArr = Array.from(responses, ([questionId, response]) => ({questionId, response}));
    const requestOptions = {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({responses: responsesArr}),
    };

    // Send request and await response from server
    const response = await fetch(`survey/${surveyId}`, requestOptions);
    const status = {
        success: false,
        body: {
            message: "Failed to submit survey response"
        }
    }
    if (response.ok) {
        status.success = true;
        status.body = await response.json();
    }
    return status;
}