import {useState, createContext} from "react";

export const SurveyContext = createContext();

export const SurveyProvider = (props) => {
    const [survey, setSurvey] = useState();

    return (
        <SurveyContext.Provider value={[survey, setSurvey]}>
            {props.children}
        </SurveyContext.Provider>
    )
}