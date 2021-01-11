# AmazonChallenge
This is the Amazon Reddit Challenge

# Methodology
Worked with GitFlow

# Guidelines
Applied Clean Architecture -> 'domain' is a pure kotlin module. Actions, models and interfaces (repositories) are here.
                           -> 'infrastructure' is an android library. Clients, BE entities and action implementations are here.
                          
Applied SOLID principles
Applied code convention -> (https://github.com/futurice/android-best-practices)

# Technical
Android Jetpack,
MVVM,
Coroutines,
KOIN (Dependency Injection),
Glide,
Retrofit,
View Binding,
JUNIT

# Design
Custom fonts (roboto),
Dark Theme support,
Icons

# Unit Test
Mocking library -> Mockk (https://mockk.io),
Unit test for 'domain' Actions,
Unit test for 'app' View Models,
(Given, when then)

# Important!
The app has 2 flavours 'local' and 'network'.
'local reads from local file 'top.json'.
'network' consumes Reddit API.

# Paging
Evaluated the use of Paging 3.0, but unfortunetelly the lists are immutable, unless using a room db, which considered as a boilerplate.




