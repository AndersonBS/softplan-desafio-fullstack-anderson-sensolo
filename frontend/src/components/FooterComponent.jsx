import React, { Component } from "react";
import { withRouter } from "react-router-dom";

class FooterComponent extends Component {
    render() {
        return (
            <footer className="footer fixed-bottom bg-dark text-center">
                <span className="text-muted">
                    © 2021 Copyright:
                    <a href="https://www.linkedin.com/in/andersonsensolo/" target="_blank"> Anderson B. Sensolo</a>
                </span>
            </footer>
        );
    }
}

export default withRouter(FooterComponent);