import {useContext, useState} from 'react';

import {Button} from 'primereact/button';
import {Avatar} from 'primereact/avatar';
import {Sidebar} from 'primereact/sidebar';

import LoginDialog from "./welcome/LoginDialog.jsx";
import {UserContext} from "./context/UserContext.jsx";
import {logoutUser} from "./api/userApi.js";
import Account from "./home/Account.jsx";

const MenuBar = ({toast}) => {
    const [loginVisible, setLoginVisible] = useState(false);
    const [sidebarVisible, setSidebarVisible] = useState(false);
    const [user, setUser] = useContext(UserContext);

    const handleLogout = async () => {

        const logoutStatus = await logoutUser();
        if (logoutStatus.success) {
            setUser(null);
        } else {
            toast.current.show({
                severity: 'error',
                life: 3000,
                summary: 'Logout Error',
                detail: logoutStatus.body.message,
            });
        }
    };

    return (
        <>
            <LoginDialog visible={loginVisible} setVisible={setLoginVisible}/>
            <Sidebar className="w-full w-25rem" visible={sidebarVisible} onHide={() => setSidebarVisible(false)} position="right">
                <Account/>
            </Sidebar>
            <div className="absolute top-0 right-0 m-4">
                {user ? (
                    <div className="flex gap-2 align-items-center">
                        <Button label="Logout" icon="pi pi-sign-out" size="small"
                                style={{boxShadow: "none"}} onClick={handleLogout}/>
                        <Avatar className="flex bg-primary" label={user.firstName.substring(0, 1)} shape="circle"
                                size="large" onClick={() => setSidebarVisible(true)}/>
                    </div>
                ) : (
                    <Button label="Login" icon="pi pi-sign-in" size="small"
                            style={{boxShadow: "none"}} onClick={() => setLoginVisible(true)}/>
                )}
            </div>
        </>
    );
}

export default MenuBar;