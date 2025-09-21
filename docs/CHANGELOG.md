# Changelog

All notable changes to LaunchAlertKit will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.0.2] - 2024-12-XX

### Added
- 5 different UI styles (FullScreen1, Popover1-4)
- Multi-language support with localized content
- Comprehensive error handling with retry mechanisms
- Custom view support for advanced customization
- Analytics tracking for VIEW and CONFIRM actions
- Comprehensive unit and UI tests
- JaCoCo code coverage reporting
- External error handling library integration
- Header-based API authentication
- Configurable API URL via local.properties
- Smart UUID management to prevent duplicate alerts

### Changed
- Improved API response handling
- Enhanced error states management
- Better state flow architecture
- API parameters moved from query to headers
- Base URL now configurable via local.properties
- Error handling now uses external library

### Fixed
- Memory leaks in ViewModel
- Dialog dismissal issues
- Image loading edge cases

## [0.0.1] - 2024-12-XX

### Added
- Initial release
- Basic launch alert functionality
- Simple UI implementation
- API integration
- Basic configuration options
