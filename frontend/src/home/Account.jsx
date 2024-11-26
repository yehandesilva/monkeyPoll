import {useContext} from "react";
import {UserContext} from "../context/UserContext.jsx";

const Account = () => {
    const [user] = useContext(UserContext);

    return (
        <>
            {(user) ?
                <>
                    <div className="font-medium text-3xl text-900 mb-3">Account</div>
                    <div className="mt-2">
                        <ul className="list-none p-0 m-0">
                            <li className="flex border-top-1 mt-2">
                                <div className="flex align-items-center flex-wrap w-full m-1">
                                    <div className="text-500 w-6 font-medium">First Name:</div>
                                    <div className="text-900 w-6">
                                        {user.firstName}
                                    </div>
                                </div>
                            </li>
                            <li className="flex border-top-1 mt-2">
                                <div className="flex align-items-center flex-wrap w-full m-1">
                                    <div className="text-500 w-6 font-medium">Last Name:</div>
                                    <div className="text-900 w-6">
                                        {user.lastName}
                                    </div>
                                </div>
                            </li>
                            <li className="flex border-top-1 mt-2 border-bottom-1 pb-2">
                                <div className="flex align-items-center flex-wrap w-full m-1">
                                    <div className="text-500 w-6 font-medium">Email:</div>
                                    <div className="text-900 w-6">
                                        {user.email}
                                    </div>
                                </div>
                            </li>
                        </ul>
                    </div>
                </>
                :
                null
            }
        </>
    )
}

export default Account;