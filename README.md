# Android code

These listings contain a code fragment that implements the registration functionality. The project is written using the MVP (Model-View-Presenter) + Clean Architecture architecture. MVP is an approach related to the organization and interaction of the layer containing the visual interface of the application with the rest of the layers (such as the business logic layer, layers of data sources, etc.). All the logic of data binding and display is transferred to another class (Presenter), which serves as a binding mechanism.

Except the display layer (for which MVP is responsible), Clean Architecture includes layers of business logic (Interactors), and layers of repositories (Repositories). The layers of business logic are responsible for specific application logic, calculations, interactions with data sources, etc. 

Repository layers are responsible for a unified interface to interact with data sources, whether there are local databases, requests to an external server, or something else. Other users of this layer (for example, the layer of business logic) do not know what specific data source they interact with, which allows the system to achieve scalability and interchangeability of its individual components.

The project uses the Dependency Injection approach, which allows to have less interconnection between application components. To implement Dependency Injection, the Dagger 2 library is used. The biggest part of the code used in the application is written in a reactive style using the RxJava 2 library that’s why we achieved flexibility, simplicity and scalability of the project. This means that when you add a new functionality, there will be small probability of any errors and the development speed will be high.
