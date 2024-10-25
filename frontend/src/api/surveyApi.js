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