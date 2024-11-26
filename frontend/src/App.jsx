import 'primereact/resources/themes/lara-dark-green/theme.css';
import "primeicons/primeicons.css";
import "primeflex/primeflex.css";

import Welcome from "./welcome/Welcome.jsx";
import MenuBar from "./MenuBar.jsx";
import {useContext} from "react";
import Home from "./home/Home.jsx";
import Survey from "./survey/Survey.jsx";
import {SurveyContext} from "./context/SurveyContext.jsx";
import {UserContext} from "./context/UserContext.jsx";

const App = () => {

    // Get reference to survey and user context
    const [survey] = useContext(SurveyContext);
    const [user] = useContext(UserContext);

    return (
        <div className="h-auto min-h-screen w-full relative" style={{backgroundColor: '#014F5E'}}>
            <MenuBar/>
            {
                (survey) ? <Survey/>
                : (user) ? <Home/>
                : <Welcome/>
            }
        </div>
    )
}

export default App
