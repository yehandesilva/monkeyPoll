import {useState, createContext} from "react";
import PropTypes from "prop-types";

export const SurveyContext = createContext();

export const SurveyProvider = ({ children }) => {
    const [survey, setSurvey] = useState(null);

    return (
        <SurveyContext.Provider value={[survey, setSurvey]}>
            {children}
        </SurveyContext.Provider>
    )
};

SurveyProvider.propTypes = {
    children: PropTypes.node.isRequired
};