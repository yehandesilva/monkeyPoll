import { useContext, useState } from 'react';
import { UserContext } from "../context/UserContext.jsx";
import { Button } from 'primereact/button';
import LoginDialog from '../welcome/LoginDialog.jsx';
import CreateSurvey from "./CreateSurvey.jsx";
import {DataTable} from "primereact/datatable";
import {Column} from "primereact/column";

const Home = () => {
    const [user] = useContext(UserContext);
    const [loginDialogVisible, setLoginDialogVisible] = useState(false);
    const [showCreateSurvey, setShowCreateSurvey] = useState(false)


    if (showCreateSurvey) {
        return <CreateSurvey/>
    }

    return (
        <div className="home flex flex-column align-items-center justify-content-center h-screen">
            {user ? (
                <div>
                    <div className="flex justify-content-center">
                        <img src="monkeypoll-full-white.svg" alt="MonkeyPoll Logo"
                             style={{width: '50%', maxWidth: '1080px', height: 'auto'}}/>
                </div>
                <div className="flex flex-column align-items-center gap-3 p-4 card">
                    <h2>Welcome, {user.firstName} {user.lastName}!</h2>
                    <p className="text-secondary">We’re glad you’re here.</p>
                    <div className="user-info mt-3 text-left">
                        <p><strong>First Name:</strong> {user.firstName}</p>
                        <p><strong>Last Name:</strong> {user.lastName}</p>
                        <p><strong>Email:</strong> {user.email}</p>
                        <p><strong>Password:</strong> Just kidding!</p>
                    </div>
                    <DataTable value={user.surveys}  tableStyle={{ minWidth: '50rem' }}>
                        <Column field="surveyId" header="ID"></Column>
                        <Column field="description" header="Name"></Column>
                        <Column field="completions.length" header="Completions"></Column>
                    </DataTable>
                    <Button label="Create Survey" icon="pi pi-plus" size="small"
                            style={{ boxShadow: "none" }} onClick={() => setShowCreateSurvey(true)} />
                </div>
            </div>
            ) : (
                <div className="flex flex-column align-items-center gap-3">
                    <Button label="Login" onClick={() => setLoginDialogVisible(true)} />
                    <LoginDialog visible={loginDialogVisible} setVisible={setLoginDialogVisible} />
                </div>
            )}
        </div>
    );
};

export default Home;
