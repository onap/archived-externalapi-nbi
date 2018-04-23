.. This work is licensed under a Creative Commons Attribution 4.0 International License.
.. http://creativecommons.org/licenses/by/4.0
.. Copyright 2018 ORANGE


Delivery
========

NBI Dockers Containers Structure
================================

Below is a diagram of the ExternalAPI/NBI project docker containers and the connections between them.

.. blockdiag::


    blockdiag delivery {
        node_width = 170;
        orientation = portrait;
        MongoDB[shape = flowchart.database]
        MariaDB[shape = flowchart.database]
        NBI -> MongoDB, MariaDB;
        group bi_group {
            color = yellow;
            label = "Business Layer"
            NBI;
        }
        group data_storage_group {
            color = orange;
            label = "Data Storage Layer"
            MongoDB; MariaDB;
        }
    }

