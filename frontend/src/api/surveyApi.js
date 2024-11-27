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

    // TODO Remove survey response stub with actual returned data
    return {
        success: true,
        body: {
            description: "Tell us a little bit about you.",
            responses: [
                {
                    question: "How would you describe yourself?",
                    questionType: "TextQuestion",
                    responses: [
                        "Hardworking and focused",
                        "Creative problem solver",
                        "Curious and adaptable",
                        "Optimistic and driven",
                        "Detail-oriented thinker",
                        "Resilient and calm",
                        "Compassionate listener",
                        "Energetic and resourceful",
                        "Goal-oriented leader",
                        "Empathetic and kind",
                        "Patient and persistent",
                        "Innovative and bold",
                        "Organized and reliable",
                        "Motivated self-starter",
                        "Open-minded collaborator"
                    ],
                },
                {
                    question: "How old are you?",
                    questionType: "NumberQuestion",
                    responses: [
                        {"18" : 3},
                        {"19": 6},
                        {"21": 1},
                        {"22": 3},
                        {"23": 2},
                    ]
                },
                {
                    question: "What do you prefer?",
                    questionType: "ChoiceQuestion",
                    responses: [
                        {"Lasagna" : 10},
                        {"Soup": 5},
                    ]
                },
            ]
        }
    }

    const requestOptions = {
        method: 'GET',
    };

    const response = await fetch(`/survey/${surveyId}/results`, requestOptions);

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

// Get AI generated prompts for survey questions
// Return:
// - Success: True if prompts are generated
// - Body - The questions in JSON format or message describing the error
export const getAiQuestions = async (prompt) => {

    // TODO Remove stub replicating AI response
    return {
        success: true,
        body: {
            responses: [
                "Sample Prompt 1",
                "Sample Prompt 2",
                "Sample Prompt 3",
            ]
        }
    }

    const requestOptions = {
        method: 'GET',
    };

    const response = await fetch(`/generatePrompts/${prompt}`, requestOptions);

    const status = {
        success: false,
        body: {
            message: "Failed to get prompts"
        }
    }

    if (response.ok) {
        status.success = true;
        status.body = await response.json();
    }

    return status;
}

