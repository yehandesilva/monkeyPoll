import {useContext, useState} from 'react';

import {Button} from 'primereact/button';

import LoginDialog from "./welcome/LoginDialog.jsx";
import {UserContext} from "./context/UserContext.jsx";


const MenuBar = () => {
    const [loginVisible, setLoginVisible] = useState(false);
    const [user, setUser] = useContext(UserContext);

    const handleLogout = () => {
        setUser(null);
    };

    return (
        <>
            <LoginDialog visible={loginVisible} setVisible={setLoginVisible} />
            {user ? (
                <Button label="Logout" icon="pi pi-sign-out" size="small" className="absolute top-0 right-0 m-4"
                        style={{ boxShadow: "none" }} onClick={handleLogout} />
            ) : (
                <Button label="Login" icon="pi pi-sign-in" size="small" className="absolute top-0 right-0 m-4"
                        style={{ boxShadow: "none" }} onClick={() => setLoginVisible(true)} />
            )}
        </>
    );
}

export default MenuBar;