import 'primereact/resources/themes/lara-dark-green/theme.css';
import "primeicons/primeicons.css";
import "primeflex/primeflex.css";

import {SurveyProvider} from "./context/SurveyContext.jsx";
import {UserProvider} from "./context/UserContext.jsx";
import Welcome from "./welcome/Welcome.jsx";
import MenuBar from "./MenuBar.jsx";
import { SurveyContext } from "../context/SurveyContext.jsx";
import { UserContext } from "../context/UserContext.jsx";
import {useContext, useEffect} from "react";
import Home from "./home/Home.jsx";
import Survey from "./survey/Survey.jsx";

const App = () => {

    const [survey] = useContext(SurveyContext);
    const [user] = useContext(UserContext);
    useEffect(() => {
        console.log("HELLOOOOOO");
        console.log(survey);
    },[survey])

    console.log("hello");

    return (
        <div className="h-auto min-h-screen w-full relative" style={{backgroundColor: '#014F5E'}}>
            <MenuBar/>
            { survey ? (
                <Survey />
            ) : user ? (
                <Home />
            ) : <Welcome/>}
        </div>
    )
}

export default App
