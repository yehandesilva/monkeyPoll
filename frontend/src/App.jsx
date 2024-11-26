import 'primereact/resources/themes/lara-dark-green/theme.css';
import "primeicons/primeicons.css";
import "primeflex/primeflex.css";

import Welcome from "./welcome/Welcome.jsx";
import MenuBar from "./MenuBar.jsx";
import {useContext, useRef} from "react";
import Home from "./home/Home.jsx";
import Survey from "./survey/Survey.jsx";
import {SurveyContext} from "./context/SurveyContext.jsx";
import {UserContext} from "./context/UserContext.jsx";
import {Toast} from "primereact/toast";

const App = () => {

    // Get reference to survey and user context
    const [survey] = useContext(SurveyContext);
    const [user] = useContext(UserContext);

    const toast = useRef(null);

    return (
        <>
            <Toast ref={toast}/>
            <div className="h-auto min-h-screen w-full relative" style={{backgroundColor: '#014F5E'}}>
                <MenuBar toast={toast}/>
                {
                    (survey) ? <Survey toast={toast}/>
                        : (user) ? <Home toast={toast}/>
                            : <Welcome toast={toast}/>
                }
            </div>
        </>
    )
}

export default App;
